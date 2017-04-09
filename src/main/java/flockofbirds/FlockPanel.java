package flockofbirds;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.Timer;

import flockofbirds.actions.FlyBirds;
import flockofbirds.attributes.FlockingListener;
import flockofbirds.attributes.ExhaustedListener;
import flockofbirds.entities.Joiner;
import flockofbirds.entities.Field;
import flockofbirds.entities.Loner;
import flockofbirds.entities.Flocker;
import flockofbirds.entities.Bird;
import flockofbirds.entities.TransformationListener;
import jalse.DefaultJALSE;
import jalse.JALSE;
import jalse.entities.Entities;
import jalse.entities.Entity;

@SuppressWarnings("serial")
public class FlockPanel extends JPanel implements ActionListener, MouseListener {

    public static final int TICK_INTERVAL = 1000 / 30;

    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;

    private static void drawElement(final Graphics g, final Bird bird) {
	final Point position = bird.getPosition();
	final int size = FlockProperties.getSize();
	g.setColor(Color.BLACK);
	g.fillOval(position.x - 2, position.y - 2, size + 4, size + 4);
	g.setColor(bird.getColour());
	g.fillOval(position.x, position.y, size, size);
    }

    private final JALSE jalse;

    public FlockPanel() {
	// Manually ticked JALSE
	jalse = new DefaultJALSE.Builder().setManualEngine().build();
	// Create data model
	createEntities();
	// Size to field size
	setPreferredSize(getField().getSize());
	// Set black background
	setBackground(Color.BLACK);
	// Listener for key events
	setFocusable(true);
	addMouseListener(this);
	// Start ticking and rendering (30 FPS)
	new Timer(TICK_INTERVAL, this).start();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	// Tick model
	jalse.resume();
	// Request repaint
	repaint();
    }

    private void addBirdAtRandomPosition() {
	final Bird bird = getField().newEntity(Bird.class);
	bird.setPosition(randomPosition());
	bird.setAngle(randomAngle());
	bird.addAttributeListener(Joiner.FLOCKING_PERCENTAGE_TYPE, new FlockingListener());
	bird.addAttributeListener(Flocker.WEARINESS_PERCENTAGE_TYPE, new ExhaustedListener());
	bird.addEntityTypeListener(new TransformationListener());
	bird.markAsType(Loner.class);
    }

    public void adjustFlockerSpeed() {
	final double speed = FlockProperties.getSpeed(Flocker.class);
	getField().streamEntitiesOfType(Flocker.class).forEach(p -> p.setSpeed(speed));
    }

    public void adjustPopulation() {
	final int population = FlockProperties.getPopulation();
	int count = getField().getEntityCount();
	// Increase population
	while (count < population) {
	    addBirdAtRandomPosition();
	    count++;
	}
	// Decrease population
	while (count > population) {
	    removeRandomBird();
	    count--;
	}
    }

    public void adjustSightRange(final Class<? extends Bird> type) {
	final int sightRange = FlockProperties.getSightRange(type);
	getField().streamEntitiesOfType(type).forEach(p -> p.setSightRange(sightRange));
    }

    private void createEntities() {
	// Create field
	final Field field = jalse.newEntity(Field.ID, Field.class);
	field.setSize(new Dimension(WIDTH, HEIGHT));
	field.scheduleForActor(new FlyBirds(), 0, TICK_INTERVAL, TimeUnit.MILLISECONDS);

	// Create randomly-placed loner birds
	reset();
    }

    private Field getField() {
	return jalse.getEntityAsType(Field.ID, Field.class);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	// enFlock clicked bird(s)
	final Point point = e.getPoint();
	final int size = FlockProperties.getSize();
	getField().streamBirds().filter(p -> {
	    final Point pos = p.getPosition();
	    return pos.x - 5 <= point.x && pos.x + size + 5 >= point.x && pos.y - 5 <= point.y
		    && pos.y + size + 5 >= point.y;
	}).forEach(p -> {
	    // enflock if not flocking already
	    if (p.unmarkAsType(Loner.class) || p.unmarkAsType(Joiner.class)) {
		FlockingListener.enflockBird(p);
	    }
	});
    }

    @Override
    public void mouseEntered(final MouseEvent e) {}

    @Override
    public void mouseExited(final MouseEvent e) {}

    @Override
    public void mousePressed(final MouseEvent e) {}

    @Override
    public void mouseReleased(final MouseEvent e) {}

    @Override
    protected void paintComponent(final Graphics g) {
	// Draw component as before
	super.paintComponent(g);

	// Draw people
	getField().streamBirds().forEach(p -> drawElement(g, p));

	// Sync (Linux fix)
	Toolkit.getDefaultToolkit().sync();
    }

    private Double randomAngle() {
	final Random rand = ThreadLocalRandom.current();
	return rand.nextDouble() * Math.PI * 2;
    }

    private Point randomPosition() {
	final int size = FlockProperties.getSize();
	final Random rand = ThreadLocalRandom.current();
	return new Point(size + rand.nextInt(WIDTH), size + rand.nextInt(HEIGHT));
    }

    private void removeRandomBird() {
	Entities.randomEntity(getField()).ifPresent(Entity::kill);
    }

    public void reset() {
	// Kill them all
	getField().killEntities();
	// Create randomly-placed healthy people
	final int population = FlockProperties.getPopulation();
	for (int i = 0; i < population; i++) {
	    addBirdAtRandomPosition();
	}
    }
}

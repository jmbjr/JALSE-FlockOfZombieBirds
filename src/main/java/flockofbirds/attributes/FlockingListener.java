package flockofbirds.attributes;

import static flockofbirds.FlockPanel.TICK_INTERVAL;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import flockofbirds.FlockProperties;
import flockofbirds.actions.Exhaust;
import flockofbirds.entities.Joiner;
import flockofbirds.entities.Loner;
import flockofbirds.entities.Flocker;
import flockofbirds.entities.Bird;
import jalse.attributes.AttributeEvent;
import jalse.attributes.AttributeListener;
import jalse.entities.Entity;

public class FlockingListener implements AttributeListener<Double> {

    public static void enflockBird(final Bird b) {
	b.cancelAllScheduledForActor();
	b.markAsType(Flocker.class);
	b.scheduleForActor(new Exhaust(), TICK_INTERVAL, TICK_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public void attributeAdded(final AttributeEvent<Double> event) {
	final Bird bird = ((Entity) event.getContainer()).asType(Bird.class);
	final double flocking = event.getValue();

	// Check if flocking
	if (flocking >= 1.0) {
	    bird.unmarkAsType(Joiner.class);
	    enflockBird(bird);
	    return;
	}

	// Base colours
	final Color lonerColour = FlockProperties.getColour(Loner.class);
	final Color flockingColour = FlockProperties.getColour(Flocker.class);

	// Gradually transition from "loner" to "flocking" color
	final float r = (float) ((lonerColour.getRed() * (1. - flocking) + flockingColour.getRed() * flocking)
		/ 255.);
	final float g = (float) ((lonerColour.getGreen() * (1. - flocking) + flockingColour.getGreen() * flocking)
		/ 255.);
	final float b = (float) ((lonerColour.getBlue() * (1. - flocking) + flockingColour.getBlue() * flocking)
		/ 255.);

	bird.setColour(new Color(r, g, b));
    }
}
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

    public static void enflockBird(final Bird p) {
	p.cancelAllScheduledForActor();
	p.markAsType(Flocker.class);
	p.scheduleForActor(new Exhaust(), TICK_INTERVAL, TICK_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public void attributeAdded(final AttributeEvent<Double> event) {
	final Bird person = ((Entity) event.getContainer()).asType(Bird.class);
	final double infection = event.getValue();

	// Check infected
	if (infection >= 1.0) {
	    person.unmarkAsType(Joiner.class);
	    enflockBird(person);
	    return;
	}

	// Base colours
	final Color healthyColour = FlockProperties.getColour(Loner.class);
	final Color infectedColour = FlockProperties.getColour(Flocker.class);

	// Gradually transition from "healthy" to "infected" color
	final float r = (float) ((healthyColour.getRed() * (1. - infection) + infectedColour.getRed() * infection)
		/ 255.);
	final float g = (float) ((healthyColour.getGreen() * (1. - infection) + infectedColour.getGreen() * infection)
		/ 255.);
	final float b = (float) ((healthyColour.getBlue() * (1. - infection) + infectedColour.getBlue() * infection)
		/ 255.);

	person.setColour(new Color(r, g, b));
    }
}
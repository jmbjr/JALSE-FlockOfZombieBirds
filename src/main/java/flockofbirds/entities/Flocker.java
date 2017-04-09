package flockofbirds.entities;

import static flockofbirds.FlockPanel.TICK_INTERVAL;
import static jalse.attributes.Attributes.newNamedDoubleType;

import java.util.concurrent.TimeUnit;

import flockofbirds.actions.StartJoining;
import jalse.attributes.NamedAttributeType;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

public interface Flocker extends Bird {

    final NamedAttributeType<Double> WEARINESS_PERCENTAGE_TYPE = newNamedDoubleType("wearinessPercentage");

    public default void join(final Bird bird) {
	bird.unmarkAsType(Loner.class);
	bird.markAsType(Joiner.class);
	bird.scheduleForActor(new StartJoining(), TICK_INTERVAL, TICK_INTERVAL, TimeUnit.MILLISECONDS);

	// Restart the Exhaust() counter
	setWeariedPercentage(0.0);
    }

    @GetAttribute
    double getWeariedPercentage();

    @SetAttribute
    void setWeariedPercentage(double wearinessPercentage);
}
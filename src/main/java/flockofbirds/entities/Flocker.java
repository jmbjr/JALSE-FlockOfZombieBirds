package flockofbirds.entities;

import static flockofbirds.FlockPanel.TICK_INTERVAL;
import static jalse.attributes.Attributes.newNamedDoubleType;

import java.util.concurrent.TimeUnit;

import flockofbirds.actions.GetSick;
import jalse.attributes.NamedAttributeType;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

public interface Flocker extends Bird {

    final NamedAttributeType<Double> HUNGER_PERCENTAGE_TYPE = newNamedDoubleType("hungerPercentage");

    public default void bite(final Bird person) {
	person.unmarkAsType(Loner.class);
	person.markAsType(Joiner.class);
	person.scheduleForActor(new GetSick(), TICK_INTERVAL, TICK_INTERVAL, TimeUnit.MILLISECONDS);

	// Restart the Starve() counter
	setHungerPercentage(0.0);
    }

    @GetAttribute
    double getHungerPercentage();

    @SetAttribute
    void setHungerPercentage(double hungerPercentage);
}
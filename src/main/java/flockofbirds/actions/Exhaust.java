package flockofbirds.actions;

import flockofbirds.FlockProperties;
import flockofbirds.entities.Flocker;
import jalse.actions.Action;
import jalse.actions.ActionContext;
import jalse.entities.Entity;

public class Exhaust implements Action<Entity> {

    @Override
    public void perform(final ActionContext<Entity> context) throws InterruptedException {
	final Flocker flocker = context.getActor().asType(Flocker.class);

	// Flocker slowly exhausts itself after flocking
	final double weariness = flocker.getWeariedPercentage() + 1. / 30 / FlockProperties.getExhaustTime();
	flocker.setWeariedPercentage(weariness);
    }
}

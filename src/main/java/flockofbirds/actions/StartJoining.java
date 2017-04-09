package flockofbirds.actions;

import flockofbirds.FlockProperties;
import flockofbirds.entities.Joiner;
import jalse.actions.Action;
import jalse.actions.ActionContext;
import jalse.entities.Entity;

public class StartJoining implements Action<Entity> {

    @Override
    public void perform(final ActionContext<Entity> context) throws InterruptedException {
	final Joiner joiner = context.getActor().asType(Joiner.class);

	// Increase flocking fraction a bit
	final double flocked = joiner.getFlockingPercentage() + 1. / 30 / FlockProperties.getFlockerTime();
	joiner.setFlockingPercentage(flocked);
    }
}

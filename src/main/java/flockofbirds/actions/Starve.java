package flockofbirds.actions;

import flockofbirds.FlockProperties;
import flockofbirds.entities.Flocker;
import jalse.actions.Action;
import jalse.actions.ActionContext;
import jalse.entities.Entity;

public class Starve implements Action<Entity> {

    @Override
    public void perform(final ActionContext<Entity> context) throws InterruptedException {
	final Flocker infected = context.getActor().asType(Flocker.class);

	// Infected slowly starve to death after biting
	final double hunger = infected.getHungerPercentage() + 1. / 30 / FlockProperties.getStarveTime();
	infected.setHungerPercentage(hunger);
    }
}

package flockofbirds.actions;

import flockofbirds.FlockProperties;
import flockofbirds.entities.Joiner;
import jalse.actions.Action;
import jalse.actions.ActionContext;
import jalse.entities.Entity;

public class GetSick implements Action<Entity> {

    @Override
    public void perform(final ActionContext<Entity> context) throws InterruptedException {
	final Joiner carrier = context.getActor().asType(Joiner.class);

	// Increase infection fraction a bit
	final double infection = carrier.getInfectionPercentage() + 1. / 30 / FlockProperties.getInfectionTime();
	carrier.setInfectionPercentage(infection);
    }
}

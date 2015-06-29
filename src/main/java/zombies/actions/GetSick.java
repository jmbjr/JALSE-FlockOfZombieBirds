package zombies.actions;

import jalse.actions.Action;
import jalse.actions.ActionContext;
import jalse.attributes.Attributes;
import jalse.entities.Entity;
import zombies.ZombiesProperties;
import zombies.entities.Person;

public class GetSick implements Action<Entity> {

    private double infectionFraction = 0.0;

    @Override
    public void perform(final ActionContext<Entity> context) throws InterruptedException {
	final Person person = context.getActor().asType(Person.class);

	// Increase infection fraction a bit
	infectionFraction += 1. / 30 / ZombiesProperties.getInfectionTime();
	person.setAttribute("infectionFraction", Attributes.DOUBLE_TYPE, infectionFraction);
    }
}

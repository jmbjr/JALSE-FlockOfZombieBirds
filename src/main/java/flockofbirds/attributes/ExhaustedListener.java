package flockofbirds.attributes;

import flockofbirds.entities.Exhausted;
import flockofbirds.entities.Flocker;
import flockofbirds.entities.Bird;
import jalse.attributes.AttributeEvent;
import jalse.attributes.AttributeListener;
import jalse.entities.Entity;

public class ExhaustedListener implements AttributeListener<Double> {

    @Override
    public void attributeAdded(final AttributeEvent<Double> event) {
	final Bird person = ((Entity) event.getContainer()).asType(Bird.class);
	final double starvation = event.getValue();

	// Check starvation
	if (starvation >= 1.0) {
	    person.cancelAllScheduledForActor();
	    person.unmarkAsType(Flocker.class);
	    person.markAsType(Exhausted.class);
	}
    }
}

package flockofbirds.entities;

import flockofbirds.FlockProperties;
import jalse.entities.Entity;
import jalse.entities.EntityTypeEvent;
import jalse.entities.EntityTypeListener;

public class TransformationListener implements EntityTypeListener {

    @Override
    public void entityMarkedAsType(final EntityTypeEvent event) {
	final Bird person = event.getEntity().asType(Bird.class);
	final Class<? extends Entity> type = event.getTypeChange();

	person.setColour(FlockProperties.getColour(type));
	person.setSightRange(FlockProperties.getSightRange(type));
	person.setSpeed(FlockProperties.getSpeed(type));
    }
}

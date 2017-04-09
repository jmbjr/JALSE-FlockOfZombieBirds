package flockofbirds.entities;

import flockofbirds.FlockProperties;
import jalse.entities.Entity;
import jalse.entities.EntityTypeEvent;
import jalse.entities.EntityTypeListener;

public class TransformationListener implements EntityTypeListener {

    @Override
    public void entityMarkedAsType(final EntityTypeEvent event) {
	final Bird bird = event.getEntity().asType(Bird.class);
	final Class<? extends Entity> type = event.getTypeChange();

	bird.setColour(FlockProperties.getColour(type));
	bird.setSightRange(FlockProperties.getSightRange(type));
	bird.setSpeed(FlockProperties.getSpeed(type));
    }
}

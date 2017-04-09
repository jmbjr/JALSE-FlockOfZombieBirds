package flockofbirds.actions;

import static jalse.entities.Entities.isMarkedAsType;
import static jalse.entities.Entities.notMarkedAsType;
import static jalse.misc.Identifiable.not;

import java.awt.Point;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import flockofbirds.FlockPanel;
import flockofbirds.FlockProperties;
import flockofbirds.entities.Joiner;
import flockofbirds.entities.Exhausted;
import flockofbirds.entities.Field;
import flockofbirds.entities.Loner;
import flockofbirds.entities.Flocker;
import flockofbirds.entities.Bird;
import jalse.actions.Action;
import jalse.actions.ActionContext;
import jalse.entities.Entity;

public class FlyBirds implements Action<Entity> {

    private static int bounded(final int value, final int min, final int max) {
	return value < min ? min : value > max ? max : value;
    }

    public static Double directionAwayFromFlocker(final Bird bird, final Set<Bird> birds) {
	// Find closest infected to hide from
	final Optional<Bird> closestFlocker = getClosestBirdOfType(bird, birds, Flocker.class);

	// Cannot see any
	if (!closestFlocker.isPresent()) {
	    return randomDirection(bird);
	}

	final Point birdPos = bird.getPosition();
	final Point closestPos = closestFlocker.get().getPosition();

	// Away
	final int dx = birdPos.x - closestPos.x;
	final int dy = birdPos.y - closestPos.y;

	// Convert
	return Math.atan2(dy, dx);
    }

    private static Double directionToLonerAndJoin(final Bird bird, final Set<Bird> birds) {
	// Find closest healthy bird in sight
	final Optional<Bird> closestLoner = getClosestBirdOfType(bird, birds, Loner.class);

	// Check can see any
	if (!closestLoner.isPresent()) {
	    return randomDirection(bird);
	}

	// loner bird
	final Bird loner = closestLoner.get();

	// Towards
	final Point birdPos = bird.getPosition();
	final int dx = loner.getPosition().x - birdPos.x;
	final int dy = loner.getPosition().y - birdPos.y;

	// Check in range of biting
	final int size = FlockProperties.getSize();
	if (dx * dx + dy * dy < size * size) {
	    bird.asType(Flocker.class).join(loner);
	}

	// Convert
	return Math.atan2(dy, dx);
    }

    private static Optional<Bird> getClosestBirdOfType(final Bird bird, final Set<Bird> birds,
	    final Class<? extends Entity> type) {
	final Point birdPos = bird.getPosition();
	final Integer sightRange = bird.getSightRange();
	// Stream other entities of type
	return birds.stream().filter(not(bird)).filter(isMarkedAsType(type)).filter(b -> {
	    final Point pPos = b.getPosition();
	    // Within range
	    return Math.abs(pPos.x - birdPos.x) <= sightRange && Math.abs(pPos.y - birdPos.y) <= sightRange;
	}).collect(Collectors.minBy((a, b) -> {
	    final Point aPos = a.getPosition();
	    final Point bPos = b.getPosition();
	    // Closest bird
	    final int d1 = (aPos.x - birdPos.x) * (aPos.x - birdPos.x)
		    + (aPos.y - birdPos.y) * (aPos.y - birdPos.y);
	    final int d2 = (bPos.x - birdPos.x) * (bPos.x - birdPos.x)
		    + (bPos.y - birdPos.y) * (bPos.y - birdPos.y);
	    return d1 - d2;
	}));
    }

    private static Double randomDirection(final Bird bird) {
	return bird.getAngle() + 2. * (ThreadLocalRandom.current().nextDouble() - 0.5);
    }

    @Override
    public void perform(final ActionContext<Entity> context) throws InterruptedException {
	final Field field = context.getActor().asType(Field.class);
	final Set<Bird> birds = field.getEntitiesOfType(Bird.class);
	
	birds.stream().filter(notMarkedAsType(Exhausted.class)).forEach(bird -> {
	    // Get correct move angle
	    double moveAngle;
	    if (bird.isMarkedAsType(Flocker.class)) {
		// Move towards loner
		moveAngle = directionToLonerAndJoin(bird, birds);
	    } else if (bird.isMarkedAsType(Joiner.class)) {
		// Move randomly
		moveAngle = randomDirection(bird);
	    } else {
		// Run away
		moveAngle = directionAwayFromFlocker(bird, birds);
	    }
	    bird.setAngle(moveAngle);

	    // Calculate move delta
	    final double moveDist = bird.getSpeed();
	    final Point moveDelta = new Point((int) (moveDist * Math.cos(moveAngle)),
		    (int) (moveDist * Math.sin(moveAngle)));

	    // Original values
	    final Point pos = bird.getPosition();
	    final int size = FlockProperties.getSize();

	    // Apply bounded move delta
	    final int x = bounded(pos.x + moveDelta.x, 0, FlockPanel.WIDTH - size);
	    final int y = bounded(pos.y + moveDelta.y, 0, FlockPanel.HEIGHT - size);

	    if (pos.x != x || pos.y != y) {
		// Update if changed
		bird.setPosition(new Point(x, y));
	    }
	});
    }
}

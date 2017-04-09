package flockofbirds;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import flockofbirds.entities.Joiner;
import flockofbirds.entities.Exhausted;
import flockofbirds.entities.Loner;
import flockofbirds.entities.Flocker;
import flockofbirds.entities.Bird;
import jalse.entities.Entity;

public class FlockProperties {

    private static class BirdProperties {

	private final AtomicReference<Color> colour;
	private final AtomicInteger sightRange;
	private final AtomicLong speed;

	BirdProperties(final Color colour, final int sightRange, final double speed) {
	    this.colour = new AtomicReference<>(colour);
	    this.sightRange = new AtomicInteger(sightRange);
	    this.speed = new AtomicLong(Double.doubleToLongBits(speed));
	}
    }

    private static final int SIZE = 16;

    private static AtomicLong flockingTime = new AtomicLong(Double.doubleToLongBits(5));

    private static AtomicLong exhaustTime = new AtomicLong(Double.doubleToLongBits(10));

    private static AtomicInteger population = new AtomicInteger(100);

    private static Map<Class<?>, BirdProperties> props = new HashMap<>();

    static {
	props.put(Loner.class, new BirdProperties(Color.WHITE, 75, 3.0));
	props.put(Joiner.class, new BirdProperties(Color.WHITE, 75, 3.0));
	props.put(Flocker.class, new BirdProperties(Color.RED, 50, 4.5));
	props.put(Exhausted.class, new BirdProperties(Color.DARK_GRAY, 0, 0.0));
    }

    public static Color getColour(final Class<? extends Entity> type) {
	return props.get(type).colour.get();
    }

    public static double getFlockerTime() {
	return Double.longBitsToDouble(flockingTime.get());
    }

    public static int getPopulation() {
	return population.get();
    }

    public static int getSightRange(final Class<? extends Entity> type) {
	return props.get(type).sightRange.get();
    }

    public static int getSize() {
	return SIZE;
    }

    public static double getSpeed(final Class<? extends Entity> type) {
	return Double.longBitsToDouble(props.get(type).speed.get());
    }

    public static double getExhaustTime() {
	return Double.longBitsToDouble(exhaustTime.get());
    }

    public static void setLonerSightRange(final int sightRange) {
	setSightRange(Loner.class, sightRange);
    }

    public static void setFlockerRelativeSpeed(final double percentage) {
	final double fraction = percentage / 100;
	props.get(Flocker.class).speed.set(Double.doubleToLongBits(fraction * getSpeed(Loner.class)));
    }

    public static void setFlockerSightRange(final int sightRange) {
	setSightRange(Flocker.class, sightRange);
    }

    public static void setFLockingTime(final double flockingTime) {
	FlockProperties.flockingTime.set(Double.doubleToLongBits(flockingTime));
    }

    public static void setPopulation(final int population) {
	FlockProperties.population.set(population);
    }

    private static void setSightRange(final Class<? extends Bird> type, final int sightRange) {
	props.get(type).sightRange.set(Math.max(sightRange, SIZE));
    }

    public static void setSpeed(final Class<? extends Bird> type, final double speed) {
	props.get(type).speed.set(Double.doubleToLongBits(speed));
    }

    public static void setExhaustTime(final double exhaustTime) {
	FlockProperties.exhaustTime.set(Double.doubleToLongBits(exhaustTime));
    }
}

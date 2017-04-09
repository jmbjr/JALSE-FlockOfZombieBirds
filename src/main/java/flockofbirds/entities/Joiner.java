package flockofbirds.entities;

import static jalse.attributes.Attributes.newNamedDoubleType;

import jalse.attributes.NamedAttributeType;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

public interface Joiner extends Bird {

    final NamedAttributeType<Double> FLOCKING_PERCENTAGE_TYPE = newNamedDoubleType("flockingPercentage");

    @GetAttribute
    double getFlockingPercentage();

    @SetAttribute
    void setFlockingPercentage(double flockingPercentage);
}
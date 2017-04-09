package flockofbirds.entities;

import static jalse.attributes.Attributes.newNamedDoubleType;

import jalse.attributes.NamedAttributeType;
import jalse.entities.annotations.GetAttribute;
import jalse.entities.annotations.SetAttribute;

public interface Joiner extends Bird {

    final NamedAttributeType<Double> INFECTION_PERCENTAGE_TYPE = newNamedDoubleType("infectionPercentage");

    @GetAttribute
    double getInfectionPercentage();

    @SetAttribute
    void setInfectionPercentage(double infectionPercentage);
}
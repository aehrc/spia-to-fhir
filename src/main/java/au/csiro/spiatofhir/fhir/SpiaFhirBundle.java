/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.fhir;

import au.csiro.spiatofhir.spia.SpiaDistribution;
import au.csiro.spiatofhir.spia.ValidationException;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.ValueSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpiaFhirBundle {

    private SpiaDistribution spiaDistribution;
    private Bundle bundle;

    public SpiaFhirBundle(SpiaDistribution spiaDistribution) throws IOException, ValidationException {
        this.spiaDistribution = spiaDistribution;
        transform();
    }

    private void transform() throws IOException, ValidationException {
        bundle = new Bundle();
        buildBundle();
    }

    private void buildBundle() throws IOException, ValidationException {
        List<ValueSet> valueSets = new ArrayList<>();

        // Chemical pathology
        ChemicalPathologyValueSet chemicalPathologyValueSet =
                new ChemicalPathologyValueSet(spiaDistribution.getRefset(SpiaDistribution.DistributionEntry.CHEMICAL));
        valueSets.add(chemicalPathologyValueSet.getValueSet());
        // Microbiology serology molecular
        MicrobiologySerologyMolecularValueSet microbiologySerologyMolecularValueSet =
                new MicrobiologySerologyMolecularValueSet(spiaDistribution.getRefset(SpiaDistribution.DistributionEntry.MICROBIOLOGY_SEROLOGY_MOLECULAR));
        valueSets.add(microbiologySerologyMolecularValueSet.getValueSet());
        // Haematology
        HaematologyValueSet haematologyValueSet =
                new HaematologyValueSet(spiaDistribution.getRefset(SpiaDistribution.DistributionEntry.HAEMATOLOGY));
        valueSets.add(haematologyValueSet.getValueSet());
        // Immunopathology
        ImmunopathologyValueSet immunopathologyValueSet = new ImmunopathologyValueSet(spiaDistribution.getRefset(
                SpiaDistribution.DistributionEntry.IMMUNOPATHOLOGY));
        valueSets.add(immunopathologyValueSet.getValueSet());
        // Requesting
        RequestingValueSet requestingValueSet =
                new RequestingValueSet(spiaDistribution.getRefset(SpiaDistribution.DistributionEntry.REQUESTING));
        valueSets.add(requestingValueSet.getValueSet());

        for (ValueSet valueSet : valueSets) {
            Bundle.BundleEntryComponent bundleEntry = new Bundle.BundleEntryComponent();
            bundleEntry.setResource(valueSet);
            bundle.addEntry(bundleEntry);
        }
    }

    /**
     * Returns the Bundle resource build using the supplied SPIA distribution.
     *
     * @return
     */
    public Bundle getBundle() {
        return bundle;
    }

}

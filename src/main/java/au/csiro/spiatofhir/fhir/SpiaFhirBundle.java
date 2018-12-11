/*
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.fhir;

import au.csiro.spiatofhir.spia.HasRefsetEntries;
import au.csiro.spiatofhir.spia.SpiaDistribution;
import au.csiro.spiatofhir.spia.ValidationException;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry.*;

/**
 * @author John Grimes
 */
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
        List<Resource> resources = new ArrayList<>();
        HasRefsetEntries requestingRefset = (HasRefsetEntries) spiaDistribution.getRefset(REQUESTING);
        HasRefsetEntries chemicalRefset = (HasRefsetEntries) spiaDistribution.getRefset(CHEMICAL);
        HasRefsetEntries microbiologyRefset = (HasRefsetEntries) spiaDistribution.getRefset(
                MICROBIOLOGY_SEROLOGY_MOLECULAR);
        HasRefsetEntries haematologyRefset = (HasRefsetEntries) spiaDistribution.getRefset(HAEMATOLOGY);
        HasRefsetEntries immunopathologyRefset = (HasRefsetEntries) spiaDistribution.getRefset(IMMUNOPATHOLOGY);
        HasRefsetEntries preferredUnitsRefset = (HasRefsetEntries) spiaDistribution.getRefset(PREFERRED_UNITS);

        // Requesting
        RequestingValueSet requestingValueSet = new RequestingValueSet(requestingRefset);
        resources.add(requestingValueSet.getValueSet());
        // Chemical pathology
        ChemicalPathologyValueSet chemicalPathologyValueSet = new ChemicalPathologyValueSet(chemicalRefset);
        ChemicalPathologyUnitMap chemicalPathologyUnitMap = new ChemicalPathologyUnitMap(chemicalRefset);
        resources.add(chemicalPathologyValueSet.getValueSet());
        resources.add(chemicalPathologyUnitMap.getConceptMap());
        // Microbiology serology molecular
        MicrobiologySerologyMolecularValueSet microbiologyValueSet =
                new MicrobiologySerologyMolecularValueSet(microbiologyRefset);
        MicrobiologySerologyMolecularUnitMap microbiologyUnitMap = new MicrobiologySerologyMolecularUnitMap(
                microbiologyRefset);
        resources.add(microbiologyValueSet.getValueSet());
        resources.add(microbiologyUnitMap.getConceptMap());
        // Haematology
        HaematologyValueSet haematologyValueSet = new HaematologyValueSet(haematologyRefset);
        HaematologyUnitMap haematologyUnitMap = new HaematologyUnitMap(haematologyRefset);
        resources.add(haematologyValueSet.getValueSet());
        resources.add(haematologyUnitMap.getConceptMap());
        // Immunopathology
        ImmunopathologyValueSet immunopathologyValueSet = new ImmunopathologyValueSet(immunopathologyRefset);
        ImmunopathologyUnitMap immunopathologyUnitMap = new ImmunopathologyUnitMap(immunopathologyRefset);
        resources.add(immunopathologyValueSet.getValueSet());
        resources.add(immunopathologyUnitMap.getConceptMap());
        // Preferred units
        PreferredUnitsValueSet preferredUnitsValueSet = new PreferredUnitsValueSet(preferredUnitsRefset);
        resources.add(preferredUnitsValueSet.getValueSet());

        for (Resource resource : resources) {
            Bundle.BundleEntryComponent bundleEntry = new Bundle.BundleEntryComponent();
            bundleEntry.setResource(resource);
            bundle.addEntry(bundleEntry);
        }
    }

    /**
     * Returns the Bundle resource build using the supplied SPIA distribution.
     */
    public Bundle getBundle() {
        return bundle;
    }

}

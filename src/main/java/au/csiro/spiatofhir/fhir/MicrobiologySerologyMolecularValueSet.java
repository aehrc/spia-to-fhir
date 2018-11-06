/*
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.fhir;

import au.csiro.spiatofhir.spia.HasRefsetEntries;
import org.hl7.fhir.dstu3.model.ValueSet;

/**
 * @author John Grimes
 */
public class MicrobiologySerologyMolecularValueSet implements SpiaFhirValueSet {

    private HasRefsetEntries refset;
    private ValueSet valueSet;

    public MicrobiologySerologyMolecularValueSet(HasRefsetEntries refset) {
        this.refset = refset;
        buildValueSet();
    }

    private void buildValueSet() {
        valueSet = new ValueSet();
        valueSet.setId("spia-microbiology-serology-molecular-refset-3");
        valueSet.setUrl("https://www.rcpa.edu.au/fhir/ValueSet/spia-microbiology-serology-molecular-refset-3");
        valueSet.setVersion("3.0");
        valueSet.setName("RCPA - SPIA Microbiology Serology Molecular Pathology Terminology Reference Set v3.0");
        valueSet.setTitle("RCPA - SPIA Microbiology Serology Molecular Pathology Terminology Reference Set v3.0");
        SpiaFhirValueSet.addCommonElementsToValueSet(valueSet);
        ValueSet.ValueSetComposeComponent compose = SpiaFhirValueSet.buildComposeFromEntries(refset.getRefsetEntries(),
                                                                                             "http://loinc.org");
        valueSet.setCompose(compose);
    }

    @Override
    public ValueSet getValueSet() {
        return valueSet;
    }

}

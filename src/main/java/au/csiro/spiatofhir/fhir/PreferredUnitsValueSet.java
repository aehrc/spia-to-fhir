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
public class PreferredUnitsValueSet implements SpiaFhirValueSet {

    private HasRefsetEntries refset;
    private ValueSet valueSet;

    public PreferredUnitsValueSet(HasRefsetEntries refset) {
        this.refset = refset;
        buildValueSet();
    }

    private void buildValueSet() {
        valueSet = new ValueSet();
        valueSet.setId("spia-preferred-units-refset-3");
        valueSet.setUrl("https://www.rcpa.edu.au/fhir/ValueSet/spia-preferred-units-refset-3");
        valueSet.setVersion("3.0");
        valueSet.setName("RCPA - SPIA Preferred Units Reference Set v3.0");
        valueSet.setTitle("RCPA - SPIA Preferred Units Reference Set v3.0");
        SpiaFhirValueSet.addCommonElementsToValueSet(valueSet);
        ValueSet.ValueSetComposeComponent compose =
                SpiaFhirValueSet.buildComposeFromEntries(refset.getRefsetEntries(), "http://unitsofmeasure.org");
        valueSet.setCompose(compose);
    }

    @Override
    public ValueSet getValueSet() {
        return valueSet;
    }

}

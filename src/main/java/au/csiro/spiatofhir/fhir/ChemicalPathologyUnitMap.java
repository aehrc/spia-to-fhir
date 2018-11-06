/*
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */

package au.csiro.spiatofhir.fhir;

import au.csiro.spiatofhir.spia.HasRefsetEntries;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.UriType;

/**
 * @author John Grimes
 */
public class ChemicalPathologyUnitMap {

    private HasRefsetEntries refset;
    private ConceptMap conceptMap;

    public ChemicalPathologyUnitMap(HasRefsetEntries refset) {
        this.refset = refset;
        buildConceptMap();
    }

    private void buildConceptMap() {
        conceptMap = new ConceptMap();
        conceptMap.setId("spia-chemical-pathology-unit-map-3");
        conceptMap.setVersion("3.0");
        conceptMap.setName("RCPA - SPIA Chemical Pathology Unit Map v3.0");
        conceptMap.setTitle("RCPA - SPIA Chemical Pathology Unit Map v3.0");
        conceptMap.setPurpose("Resolving RCPA specified units for members of the SPIA Chemical Pathology Reference " +
                                      "Set.");
        SpiaFhirConceptMap.addCommonElementsToConceptMap(conceptMap);
        conceptMap.setSource(new UriType("https://www.rcpa.edu.au/fhir/ValueSet/spia-chemical-pathology-refset-3"));
        conceptMap.setTarget(new UriType("https://www.rcpa.edu.au/fhir/ValueSet/spia-preferred-units-refset-3"));
        ConceptMap.ConceptMapGroupComponent group = SpiaFhirConceptMap.buildGroupFromEntries(refset.getRefsetEntries());
        group.setSource("http://loinc.org");
        group.setTarget("http://unitsofmeasure.org");
        conceptMap.getGroup().add(group);
    }

    public ConceptMap getConceptMap() {
        return conceptMap;
    }

}

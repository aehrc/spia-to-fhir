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
public class MicrobiologySerologyMolecularUnitMap {

    private HasRefsetEntries refset;
    private ConceptMap conceptMap;

    public MicrobiologySerologyMolecularUnitMap(HasRefsetEntries refset) {
        this.refset = refset;
        buildConceptMap();
    }

    private void buildConceptMap() {
        conceptMap = new ConceptMap();
        conceptMap.setId("spia-microbiology-unit-map-3");
        conceptMap.setVersion("3.0");
        conceptMap.setName("RCPA - SPIA Microbiology Serology Molecular Unit Map v3.0");
        conceptMap.setTitle("RCPA - SPIA Microbiology Serology Molecular Unit Map v3.0");
        conceptMap.setPurpose(
                "Resolving RCPA specified units for members of the SPIA Microbiology Serology Molecular Reference " +
                        "Set.");
        SpiaFhirConceptMap.addCommonElementsToConceptMap(conceptMap);
        conceptMap.setSource(new UriType("https://www.rcpa.edu.au/fhir/ValueSet/spia-microbiology-refset-3"));
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

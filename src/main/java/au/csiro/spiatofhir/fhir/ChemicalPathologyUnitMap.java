/*
 *    Copyright 2018 Australian e-Health Research Centre, CSIRO
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
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

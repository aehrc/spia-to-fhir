/*
 * Copyright 2019 Australian e-Health Research Centre, CSIRO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.csiro.spiatofhir.fhir;

import au.csiro.spiatofhir.loinc.Loinc;
import au.csiro.spiatofhir.spia.Refset;
import au.csiro.spiatofhir.ucum.Ucum;
import au.csiro.spiatofhir.utils.Strings;
import java.util.Date;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.UriType;

/**
 * @author John Grimes
 */
public class HaematologyUnitMap extends SpiaFhirConceptMap {

  @Override
  public Resource transform(Refset refset, Date publicationDate) {
    ConceptMap conceptMap = new ConceptMap();
    conceptMap.setVersion("1.1.0");
    conceptMap.setId(
        "spia-haematology-unit-map-" + Strings.majorVersionFromSemVer(conceptMap.getVersion()));
    conceptMap.setUrl("https://www.rcpa.edu.au/fhir/ConceptMap/" + conceptMap.getId());
    Identifier oid = new Identifier();
    oid.setSystem("urn:ietf:rfc:3986");
    oid.setValue("urn:oid:1.2.36.1.2001.1004.300.100.1008");
    conceptMap.setIdentifier(oid);
    conceptMap.setTitle("RCPA - SPIA Haematology Unit Map");
    conceptMap.setName("spia-haematology-unit-map");
    conceptMap.setDescription(
        "Map between the SPIA Haematology Reference Set (v3.1) and the corresponding RCPA "
            + "preferred units (v1.1) for each code.");
    conceptMap.setPurpose(
        "Resolving RCPA specified units for members of the SPIA Haematology Reference " +
            "Set.");
    conceptMap.setDate(publicationDate);
    SpiaFhirConceptMap.addCommonElementsToConceptMap(conceptMap);
    conceptMap
        .setSource(new UriType("https://www.rcpa.edu.au/fhir/ValueSet/spia-haematology-refset-1"));
    conceptMap.setTarget(
        new UriType("https://www.rcpa.edu.au/fhir/ValueSet/spia-preferred-units-refset-1"));
    ConceptMap.ConceptMapGroupComponent group = SpiaFhirConceptMap
        .buildPreferredUnitGroupFromEntries(refset.getRefsetEntries());
    group.setSource(Loinc.SYSTEM_URI);
    group.setTarget(Ucum.SYSTEM_URI);
    conceptMap.getGroup().add(group);

    return conceptMap;
  }

}

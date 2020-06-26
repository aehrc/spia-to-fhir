/*
 * Copyright 2020 Australian e-Health Research Centre, CSIRO
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

package au.csiro.spiatofhir.fhir.stu3;

import au.csiro.spiatofhir.fhir.ChemicalCombiningResultsMap;
import au.csiro.spiatofhir.fhir.ChemicalPathologyValueSet;
import au.csiro.spiatofhir.fhir.CombiningResultsCodeSystem;
import au.csiro.spiatofhir.fhir.FhirResource;
import au.csiro.spiatofhir.loinc.Loinc;
import au.csiro.spiatofhir.spia.Refset;
import java.util.Date;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.UriType;

/**
 * @author John Grimes
 */
public class Stu3ChemicalCombiningResultsMap extends Stu3Resource {

  public Stu3ChemicalCombiningResultsMap(Date publicationDate) {
    super(publicationDate);
  }

  @Override
  public Resource transform(Refset refset) {
    ConceptMap conceptMap = new ConceptMap();
    conceptMap.setVersion(FhirResource.VERSION);
    conceptMap.setId(ChemicalCombiningResultsMap.ID);
    conceptMap.setUrl(ChemicalCombiningResultsMap.URL);
    Identifier oid = new Identifier();
    oid.setSystem(FhirResource.OID_SYSTEM);
    oid.setValue("urn:oid:" + ChemicalCombiningResultsMap.OID);
    conceptMap.setIdentifier(oid);
    conceptMap.setTitle(ChemicalCombiningResultsMap.TITLE);
    conceptMap.setName(ChemicalCombiningResultsMap.NAME);
    conceptMap
        .setDescription(ChemicalCombiningResultsMap.DESCRIPTION);
    conceptMap.setPurpose(ChemicalCombiningResultsMap.PURPOSE);
    conceptMap.setDate(getPublicationDate());
    Stu3ConceptMap.addCommonElementsToConceptMap(conceptMap);
    conceptMap.setSource(new UriType(ChemicalPathologyValueSet.URL));
    conceptMap.setTarget(new UriType(CombiningResultsCodeSystem.VALUESET_URL));
    ConceptMap.ConceptMapGroupComponent group = Stu3ConceptMap
        .buildCombiningResultsFlagsGroupFromEntries(refset.getRefsetEntries());
    group.setSource(Loinc.SYSTEM_URI);
    group.setTarget(CombiningResultsCodeSystem.URL);
    conceptMap.getGroup().add(group);

    return conceptMap;
  }

}

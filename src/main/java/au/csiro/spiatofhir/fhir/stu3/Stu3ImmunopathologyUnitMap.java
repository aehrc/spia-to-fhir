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

import au.csiro.spiatofhir.fhir.FhirResource;
import au.csiro.spiatofhir.fhir.ImmunopathologyUnitMap;
import au.csiro.spiatofhir.fhir.ImmunopathologyValueSet;
import au.csiro.spiatofhir.fhir.PreferredUnitsValueSet;
import au.csiro.spiatofhir.loinc.Loinc;
import au.csiro.spiatofhir.spia.Refset;
import au.csiro.spiatofhir.ucum.Ucum;
import java.util.Date;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.UriType;

/**
 * @author John Grimes
 */
public class Stu3ImmunopathologyUnitMap extends Stu3Resource {

  public Stu3ImmunopathologyUnitMap(Date publicationDate) {
    super(publicationDate);
  }

  @Override
  public Resource transform(Refset refset) {
    ConceptMap conceptMap = new ConceptMap();

    conceptMap.setVersion(FhirResource.VERSION);
    conceptMap.setId(ImmunopathologyUnitMap.ID);
    conceptMap.setUrl(ImmunopathologyUnitMap.URL);
    Identifier oid = new Identifier();
    oid.setSystem(FhirResource.OID_SYSTEM);
    oid.setValue("urn:oid:" + ImmunopathologyUnitMap.OID);
    conceptMap.setIdentifier(oid);
    conceptMap.setTitle(ImmunopathologyUnitMap.TITLE);
    conceptMap.setName(ImmunopathologyUnitMap.NAME);
    conceptMap.setDescription(ImmunopathologyUnitMap.DESCRIPTION);
    conceptMap.setPurpose(ImmunopathologyUnitMap.PURPOSE);
    conceptMap.setDate(getPublicationDate());
    Stu3ConceptMap.addCommonElementsToConceptMap(conceptMap);
    conceptMap.setSource(new UriType(ImmunopathologyValueSet.URL));
    conceptMap.setTarget(new UriType(PreferredUnitsValueSet.URL));
    ConceptMap.ConceptMapGroupComponent group = Stu3ConceptMap
        .buildPreferredUnitGroupFromEntries(refset.getRefsetEntries());
    group.setSource(Loinc.SYSTEM_URI);
    group.setTarget(Ucum.SYSTEM_URI);
    conceptMap.getGroup().add(group);

    return conceptMap;
  }

}

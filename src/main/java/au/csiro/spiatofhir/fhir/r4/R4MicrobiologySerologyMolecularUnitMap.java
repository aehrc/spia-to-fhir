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

package au.csiro.spiatofhir.fhir.r4;

import au.csiro.spiatofhir.fhir.FhirResource;
import au.csiro.spiatofhir.fhir.MicrobiologySerologyMolecularUnitMap;
import au.csiro.spiatofhir.fhir.MicrobiologySerologyMolecularValueSet;
import au.csiro.spiatofhir.fhir.PreferredUnitsValueSet;
import au.csiro.spiatofhir.loinc.Loinc;
import au.csiro.spiatofhir.spia.Refset;
import au.csiro.spiatofhir.ucum.Ucum;
import java.util.Date;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.UriType;

/**
 * @author John Grimes
 */
public class R4MicrobiologySerologyMolecularUnitMap extends R4Resource {

  public R4MicrobiologySerologyMolecularUnitMap(Date publicationDate) {
    super(publicationDate);
  }

  @Override
  public Resource transform(Refset refset) {
    ConceptMap conceptMap = new ConceptMap();

    conceptMap.setVersion(FhirResource.VERSION);
    conceptMap.setId(MicrobiologySerologyMolecularUnitMap.ID);
    conceptMap.setUrl(MicrobiologySerologyMolecularUnitMap.URL);
    Identifier oid = new Identifier();
    oid.setSystem(FhirResource.OID_SYSTEM);
    oid.setValue("urn:oid:" + MicrobiologySerologyMolecularUnitMap.OID);
    conceptMap.setIdentifier(oid);
    conceptMap.setTitle(MicrobiologySerologyMolecularUnitMap.TITLE);
    conceptMap.setName(MicrobiologySerologyMolecularUnitMap.NAME);
    conceptMap.setDescription(MicrobiologySerologyMolecularUnitMap.DESCRIPTION);
    conceptMap.setPurpose(MicrobiologySerologyMolecularUnitMap.PURPOSE);
    conceptMap.setDate(getPublicationDate());
    R4ConceptMap.addCommonElementsToConceptMap(conceptMap);
    conceptMap.setSource(new UriType(MicrobiologySerologyMolecularValueSet.URL));
    conceptMap.setTarget(new UriType(PreferredUnitsValueSet.URL));
    ConceptMap.ConceptMapGroupComponent group = R4ConceptMap
        .buildPreferredUnitGroupFromEntries(refset.getRefsetEntries());
    group.setSource(Loinc.SYSTEM_URI);
    group.setTarget(Ucum.SYSTEM_URI);
    conceptMap.getGroup().add(group);

    return conceptMap;
  }

}

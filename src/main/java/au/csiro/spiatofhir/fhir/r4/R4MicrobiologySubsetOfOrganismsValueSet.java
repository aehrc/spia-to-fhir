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
import au.csiro.spiatofhir.fhir.MicrobiologySubsetOfOrganismsValueSet;
import au.csiro.spiatofhir.snomed.SnomedCt;
import au.csiro.spiatofhir.spia.Refset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ValueSet;

/**
 * @author John Grimes
 */
public class R4MicrobiologySubsetOfOrganismsValueSet extends R4Resource {

  public R4MicrobiologySubsetOfOrganismsValueSet(Date publicationDate) {
    super(publicationDate);
  }

  @Override
  public Resource transform(Refset refset) {
    ValueSet valueSet = new ValueSet();
   
    valueSet.setVersion(FhirResource.VERSION);
    valueSet.setId(MicrobiologySubsetOfOrganismsValueSet.ID);
    valueSet.setUrl(MicrobiologySubsetOfOrganismsValueSet.URL);
    List<Identifier> identifier = new ArrayList<>();
    Identifier oid = new Identifier();
    oid.setSystem(FhirResource.OID_SYSTEM);
    oid.setValue("urn:oid:" + MicrobiologySubsetOfOrganismsValueSet.OID);
    identifier.add(oid);
    valueSet.setIdentifier(identifier);
    valueSet.setTitle(MicrobiologySubsetOfOrganismsValueSet.TITLE);
    valueSet.setName(MicrobiologySubsetOfOrganismsValueSet.NAME);
    valueSet.setDescription(MicrobiologySubsetOfOrganismsValueSet.DESCRIPTION);
    valueSet.setDate(getPublicationDate());
    R4ValueSet.addCommonElementsToValueSet(valueSet);
    ValueSet.ValueSetComposeComponent compose = R4ValueSet
        .buildComposeFromEntries(refset.getRefsetEntries(), SnomedCt.SYSTEM_URI);
    valueSet.setCompose(compose);

    return valueSet;
  }

}

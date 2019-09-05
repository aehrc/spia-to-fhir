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

import au.csiro.spiatofhir.spia.Refset;
import au.csiro.spiatofhir.ucum.Ucum;
import au.csiro.spiatofhir.utils.Strings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ValueSet;

/**
 * @author John Grimes
 */
public class PreferredUnitsValueSet extends SpiaFhirValueSet {

  @Override
  public Resource transform(Refset refset, Date publicationDate) {
    ValueSet valueSet = new ValueSet();
    valueSet.setVersion("2.0.0");
    valueSet.setId(
        "spia-preferred-units-refset-" + Strings.majorVersionFromSemVer(valueSet.getVersion()));
    valueSet.setUrl("https://www.rcpa.edu.au/fhir/ValueSet/" + valueSet.getId());
    List<Identifier> identifier = new ArrayList<>();
    Identifier oid = new Identifier();
    oid.setSystem("urn:ietf:rfc:3986");
    oid.setValue("urn:oid:1.2.36.1.2001.1004.300.100.1011");
    identifier.add(oid);
    valueSet.setIdentifier(identifier);
    valueSet.setTitle("RCPA - SPIA Preferred Units Reference Set");
    valueSet.setName("spia-preferred-units-refset");
    valueSet.setDescription("A set of preferred units of measure for use within pathology "
        + "reporting in Australia, based on the SPIA Preferred Units Reference Set (v1.1).");
    valueSet.setDate(publicationDate);
    SpiaFhirValueSet.addCommonElementsToValueSet(valueSet);
    ValueSet.ValueSetComposeComponent compose =
        SpiaFhirValueSet
            .buildComposeFromEntries(refset.getRefsetEntries(), Ucum.SYSTEM_URI);
    valueSet.setCompose(compose);

    return valueSet;
  }

}

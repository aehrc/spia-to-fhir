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

import au.csiro.spiatofhir.spia.HasRefsetEntries;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.ValueSet;

/**
 * @author John Grimes
 */
public class ImmunopathologyValueSet implements SpiaFhirValueSet {

  private final HasRefsetEntries refset;
  private final Date publicationDate;
  private ValueSet valueSet;

  public ImmunopathologyValueSet(HasRefsetEntries refset, Date publicationDate) {
    this.refset = refset;
    this.publicationDate = publicationDate;
    buildValueSet();
  }

  private void buildValueSet() {
    valueSet = new ValueSet();
    valueSet.setId("spia-immunopathology-refset-1");
    valueSet.setUrl("https://www.rcpa.edu.au/fhir/ValueSet/spia-immunopathology-refset-1");
    valueSet.setVersion("1.0.0");
    List<Identifier> identifier = new ArrayList<>();
    Identifier oid = new Identifier();
    oid.setSystem("urn:ietf:rfc:3986");
    // TODO: Add a real OID.
    oid.setValue("urn:oid:TBD");
    identifier.add(oid);
    valueSet.setIdentifier(identifier);
    valueSet.setTitle("RCPA - SPIA Immunopathology Terminology Reference Set");
    valueSet.setName("spia-immunopathology-refset");
    valueSet.setDescription("Standard codes for use in reporting immunopathology results in "
        + "Australia, based on the SPIA Immunopathology Terminology Reference Set (v3.0).");
    valueSet.setDate(publicationDate);
    SpiaFhirValueSet.addCommonElementsToValueSet(valueSet);
    ValueSet.ValueSetComposeComponent compose = SpiaFhirValueSet
        .buildComposeFromEntries(refset.getRefsetEntries(),
            "http://loinc.org");
    valueSet.setCompose(compose);
  }

  @Override
  public ValueSet getValueSet() {
    return valueSet;
  }

}

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
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.ValueSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Grimes
 */
public class PreferredUnitsValueSet implements SpiaFhirValueSet {

    private final HasRefsetEntries refset;
    private ValueSet valueSet;

    public PreferredUnitsValueSet(HasRefsetEntries refset) {
        this.refset = refset;
        buildValueSet();
    }

    private void buildValueSet() {
        valueSet = new ValueSet();
        valueSet.setId("spia-preferred-units-refset-1");
        valueSet.setUrl("https://www.rcpa.edu.au/fhir/ValueSet/spia-preferred-units-refset-1");
        valueSet.setVersion("1.0.0");
        List<Identifier> identifier = new ArrayList<>();
        Identifier oid = new Identifier();
        oid.setSystem("urn:ietf:rfc:3986");
        // TODO: Add a real OID.
        oid.setValue("urn:oid:TBD");
        identifier.add(oid);
        valueSet.setIdentifier(identifier);
        valueSet.setName("RCPA - SPIA Preferred Units Reference Set");
        valueSet.setTitle("RCPA - SPIA Preferred Units Reference Set");
        valueSet.setDescription(
                "A set of preferred units of measure for use within pathology reporting in Australia, based on the " +
                        "SPIA Preferred Units Reference Set (v1.0).");
        SpiaFhirValueSet.addCommonElementsToValueSet(valueSet);
        valueSet.getText().getDiv().addText("RCPA - SPIA Preferred Units Reference Set");
        ValueSet.ValueSetComposeComponent compose =
                SpiaFhirValueSet.buildComposeFromEntries(refset.getRefsetEntries(), "http://unitsofmeasure.org");
        valueSet.setCompose(compose);
    }

    @Override
    public ValueSet getValueSet() {
        return valueSet;
    }

}

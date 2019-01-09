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
import au.csiro.spiatofhir.spia.SpiaDistribution;
import au.csiro.spiatofhir.spia.ValidationException;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry.*;

/**
 * @author John Grimes
 */
public class SpiaFhirBundle {

    private final SpiaDistribution spiaDistribution;
    private final FhirContext fhirContext;
    private Bundle bundle;

    public SpiaFhirBundle(FhirContext fhirContext, SpiaDistribution spiaDistribution)
            throws IOException, ValidationException {
        this.spiaDistribution = spiaDistribution;
        this.fhirContext = fhirContext;
        transform();
    }

    private void transform() throws IOException, ValidationException {
        bundle = new Bundle();
        buildBundle();
    }

    private void buildBundle() throws IOException, ValidationException {
        List<Resource> resources = new ArrayList<>();
        HasRefsetEntries requestingRefset = (HasRefsetEntries) spiaDistribution.getRefset(REQUESTING);
        HasRefsetEntries chemicalRefset = (HasRefsetEntries) spiaDistribution.getRefset(CHEMICAL);
        HasRefsetEntries microbiologyRefset = (HasRefsetEntries) spiaDistribution.getRefset(
                MICROBIOLOGY_SEROLOGY_MOLECULAR);
        HasRefsetEntries haematologyRefset = (HasRefsetEntries) spiaDistribution.getRefset(HAEMATOLOGY);
        HasRefsetEntries immunopathologyRefset = (HasRefsetEntries) spiaDistribution.getRefset(IMMUNOPATHOLOGY);
        HasRefsetEntries preferredUnitsRefset = (HasRefsetEntries) spiaDistribution.getRefset(PREFERRED_UNITS);

        // Requesting
        RequestingValueSet requestingValueSet = new RequestingValueSet(requestingRefset);
        resources.add(requestingValueSet.getValueSet());
        // Chemical pathology
        ChemicalPathologyValueSet chemicalPathologyValueSet = new ChemicalPathologyValueSet(chemicalRefset);
        ChemicalPathologyUnitMap chemicalPathologyUnitMap = new ChemicalPathologyUnitMap(chemicalRefset);
        resources.add(chemicalPathologyValueSet.getValueSet());
        resources.add(chemicalPathologyUnitMap.getConceptMap());
        // Microbiology serology molecular
        MicrobiologySerologyMolecularValueSet microbiologyValueSet =
                new MicrobiologySerologyMolecularValueSet(microbiologyRefset);
        MicrobiologySerologyMolecularUnitMap microbiologyUnitMap = new MicrobiologySerologyMolecularUnitMap(
                microbiologyRefset);
        resources.add(microbiologyValueSet.getValueSet());
        resources.add(microbiologyUnitMap.getConceptMap());
        // Haematology
        HaematologyValueSet haematologyValueSet = new HaematologyValueSet(haematologyRefset);
        HaematologyUnitMap haematologyUnitMap = new HaematologyUnitMap(haematologyRefset);
        resources.add(haematologyValueSet.getValueSet());
        resources.add(haematologyUnitMap.getConceptMap());
        // Immunopathology
        ImmunopathologyValueSet immunopathologyValueSet = new ImmunopathologyValueSet(immunopathologyRefset);
        ImmunopathologyUnitMap immunopathologyUnitMap = new ImmunopathologyUnitMap(immunopathologyRefset);
        resources.add(immunopathologyValueSet.getValueSet());
        resources.add(immunopathologyUnitMap.getConceptMap());
        // Preferred units
        PreferredUnitsValueSet preferredUnitsValueSet = new PreferredUnitsValueSet(preferredUnitsRefset);
        resources.add(preferredUnitsValueSet.getValueSet());
        // Supporting terminology
        try (InputStream designationTypeStream = getClass().getResourceAsStream(
                "/codesystem-spia-designation-type.json")) {
            CodeSystem designationType = (CodeSystem) fhirContext.newJsonParser().parseResource(new InputStreamReader(
                    designationTypeStream));
            resources.add(designationType);
        }

        for (Resource resource : resources) {
            Bundle.BundleEntryComponent bundleEntry = new Bundle.BundleEntryComponent();
            bundleEntry.setResource(resource);
            bundle.addEntry(bundleEntry);
        }
    }

    /**
     * Returns the Bundle resource build using the supplied SPIA distribution.
     */
    public Bundle getBundle() {
        return bundle;
    }

}

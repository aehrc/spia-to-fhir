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

import static au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import au.csiro.spiatofhir.spia.Refset;
import au.csiro.spiatofhir.spia.SpiaDistribution;
import au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry;
import au.csiro.spiatofhir.spia.ValidationException;
import ca.uhn.fhir.context.FhirContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Resource;

/**
 * @author John Grimes
 */
public class SpiaFhirBundle {

  // Map to the files within the distribution that contain each reference set.
  private static final Map<DistributionEntry, List<Class>> resourcesToGenerate = new EnumMap<DistributionEntry, List<Class>>(
      DistributionEntry.class) {{
    put(REQUESTING, singletonList(RequestingValueSet.class));
    put(CHEMICAL, asList(ChemicalPathologyValueSet.class, ChemicalPathologyUnitMap.class,
        ChemicalCombiningResultsMap.class));
    put(HAEMATOLOGY, asList(HaematologyValueSet.class, HaematologyUnitMap.class));
    put(IMMUNOPATHOLOGY, asList(ImmunopathologyValueSet.class, ImmunopathologyUnitMap.class));
    put(MICROBIOLOGY_SEROLOGY_MOLECULAR, asList(MicrobiologySerologyMolecularValueSet.class,
        MicrobiologySerologyMolecularUnitMap.class));
    put(MICROBIOLOGY_ORGANISMS, singletonList(MicrobiologySubsetOfOrganismsValueSet.class));
    put(DistributionEntry.PREFERRED_UNITS, singletonList(PreferredUnitsValueSet.class));
  }};
  private final SpiaDistribution spiaDistribution;
  private final FhirContext fhirContext;
  private final Date publicationDate;
  private Bundle bundle;

  public SpiaFhirBundle(FhirContext fhirContext, SpiaDistribution spiaDistribution,
      Date publicationDate)
      throws IOException, ValidationException {
    this.spiaDistribution = spiaDistribution;
    this.fhirContext = fhirContext;
    this.publicationDate = publicationDate;
    transform();
  }

  private void transform() throws IOException {
    bundle = new Bundle();
    buildBundle();
  }

  /**
   * Feeding the SPIA reference sets to the classes that know how to build the FHIR resources, then
   * puts the resulting resources into a FHIR Bundle.
   */
  private void buildBundle() throws IOException {
    // Get the SPIA reference sets which contain the source data.
    Map<DistributionEntry, Refset> refsets = spiaDistribution.getRefsets();
    List<Resource> resources = new ArrayList<>();

    // Build each of the ValueSets and ConceptMaps using the source reference sets.
    for (DistributionEntry entry : refsets.keySet()) {
      resources.addAll(resourcesToGenerate.get(entry).stream().map(fhirResource -> {
        try {
          @SuppressWarnings("unchecked") Method transform = fhirResource
              .getDeclaredMethod("transform", Refset.class, Date.class);
          @SuppressWarnings("unchecked") Constructor constructor = fhirResource.getConstructor();
          SpiaFhirResource fhirResourceInstance = (SpiaFhirResource) constructor.newInstance();
          return (Resource) transform
              .invoke(fhirResourceInstance, refsets.get(entry), publicationDate);
        } catch (InvocationTargetException e) {
          throw new RuntimeException("Error instantiating reference set parser", e.getCause());
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException e) {
          throw new RuntimeException("Error instantiating reference set parser", e);
        }
      }).collect(Collectors.toList()));
    }

    // Get supporting terminology resources from the resources directory.
    try (
        InputStream designationTypeStream = getClass().getResourceAsStream(
            "/spia-designation-type.CodeSystem.json")) {
      CodeSystem designationType = (CodeSystem) fhirContext.newJsonParser()
          .parseResource(new InputStreamReader(designationTypeStream));
      resources.add(designationType);
    }
    try (
        InputStream designationTypeStream = getClass().getResourceAsStream(
            "/spia-combining-results-flag.CodeSystem.json")) {
      CodeSystem combiningResultsFlag = (CodeSystem) fhirContext.newJsonParser()
          .parseResource(new InputStreamReader(designationTypeStream));
      resources.add(combiningResultsFlag);
    }

    // Add all resources to the Bundle.
    for (Resource resource : resources) {
      Bundle.BundleEntryComponent bundleEntry = new Bundle.BundleEntryComponent();
      bundleEntry.setResource(resource);
      bundle.addEntry(bundleEntry);
    }

    // Set the Bundle type to `collection`.
    bundle.setType(Bundle.BundleType.COLLECTION);
  }

  /**
   * Returns the Bundle resource build using the supplied SPIA distribution.
   */
  public Bundle getBundle() {
    return bundle;
  }

}

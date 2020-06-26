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

import static au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry.CHEMICAL;
import static au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry.HAEMATOLOGY;
import static au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry.IMMUNOPATHOLOGY;
import static au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry.MICROBIOLOGY_ORGANISMS;
import static au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry.MICROBIOLOGY_SEROLOGY_MOLECULAR;
import static au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry.REQUESTING;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import au.csiro.spiatofhir.spia.Refset;
import au.csiro.spiatofhir.spia.SpiaDistribution;
import au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Resource;

/**
 * @author John Grimes
 */
public class Stu3Bundle {

  private final Map<DistributionEntry, Collection<Stu3Resource>> resourcesToGenerate;
  private final SpiaDistribution spiaDistribution;
  private final Date publicationDate;
  private Bundle bundle;

  public Stu3Bundle(SpiaDistribution spiaDistribution, Date publicationDate)
      throws IOException {
    this.spiaDistribution = spiaDistribution;
    this.publicationDate = publicationDate;
    this.resourcesToGenerate = buildResources();
    transform();
  }

  private Map<DistributionEntry, Collection<Stu3Resource>> buildResources() {
    return new EnumMap<DistributionEntry, Collection<Stu3Resource>>(DistributionEntry.class) {{
      put(REQUESTING, singletonList(new Stu3RequestingValueSet(publicationDate)));
      put(CHEMICAL, asList(new Stu3ChemicalPathologyValueSet(publicationDate),
          new Stu3ChemicalPathologyUnitMap(publicationDate),
          new Stu3ChemicalCombiningResultsMap(publicationDate)));
      put(HAEMATOLOGY, asList(new Stu3HaematologyValueSet(publicationDate),
          new Stu3HaematologyUnitMap(publicationDate)));
      put(IMMUNOPATHOLOGY, asList(new Stu3ImmunopathologyValueSet(publicationDate),
          new Stu3ImmunopathologyUnitMap(publicationDate)));
      put(MICROBIOLOGY_SEROLOGY_MOLECULAR,
          asList(new Stu3MicrobiologySerologyMolecularValueSet(publicationDate),
              new Stu3MicrobiologySerologyMolecularUnitMap(publicationDate)));
      put(MICROBIOLOGY_ORGANISMS,
          singletonList(new Stu3MicrobiologySubsetOfOrganismsValueSet(publicationDate)));
      put(DistributionEntry.PREFERRED_UNITS,
          singletonList(new Stu3PreferredUnitsValueSet(publicationDate)));
    }};
  }

  private void transform() throws IOException {
    bundle = new Bundle();
    buildBundle();
  }

  /**
   * Feeding the SPIA reference sets to the classes that know how to build the FHIR resources, then
   * puts the resulting resources into a FHIR Bundle.
   */
  private void buildBundle() {
    // Get the SPIA reference sets which contain the source data.
    Map<DistributionEntry, Refset> refsets = spiaDistribution.getRefsets();
    List<Resource> resources = new ArrayList<>();

    // Build each of the ValueSets and ConceptMaps using the source reference sets.
    for (DistributionEntry entry : refsets.keySet()) {
      resources.addAll(resourcesToGenerate.get(entry).stream()
          .map(fhirResource -> fhirResource.transform(refsets.get(entry)))
          .collect(Collectors.toList()));
    }

    // Build the combining results flag CodeSystem.
    CodeSystem combiningResultsFlag = new Stu3CombiningResultsCodeSystem(publicationDate).build();
    resources.add(combiningResultsFlag);

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

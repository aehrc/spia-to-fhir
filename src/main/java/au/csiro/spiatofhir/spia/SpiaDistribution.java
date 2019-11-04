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

package au.csiro.spiatofhir.spia;

import static au.csiro.spiatofhir.spia.SpiaDistribution.DistributionEntry.*;

import au.csiro.spiatofhir.fhir.TerminologyClient;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.fhir.ucum.UcumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the distribution ZIP file used to house the SPIA distribution. Parses each reference
 * set and provides them back as objects, which can then be used by the classes responsible for the
 * FHIR transform.
 *
 * @author John Grimes
 */
public class SpiaDistribution {

  // Map to the files within the distribution that contain each reference set.
  private static final Map<DistributionEntry, String> expectedEntries = new EnumMap<DistributionEntry, String>(
      DistributionEntry.class) {{
    put(REQUESTING,
        "RCPA - SPIA Requesting Pathology Terminology Reference Set v3.1.xlsx");
    put(CHEMICAL,
        "RCPA - SPIA Chemical Pathology Terminology Reference Set v3.1.xlsx");
    put(HAEMATOLOGY,
        "RCPA - SPIA Haematology Terminology Reference Set v3.1.xlsx");
    put(IMMUNOPATHOLOGY,
        "RCPA - SPIA Immunopathology Terminology Reference Set v3.1.xlsx");
    put(MICROBIOLOGY_SEROLOGY_MOLECULAR,
        "RCPA - SPIA Microbiology Serology Molecular Path Terminology Reference Set v3.1.xlsx");
    put(MICROBIOLOGY_ORGANISMS,
        "RCPA - SPIA Microbiology Subset of Organisms v3.1.xlsx");
    put(DistributionEntry.PREFERRED_UNITS,
        "RCPA - SPIA Preferred Units v1.1.xlsx");
  }};
  private static final Logger logger = LoggerFactory.getLogger(SpiaDistribution.class);
  private ZipFile zipFile;
  private final Map<DistributionEntry, Refset> refsets = new EnumMap<>(DistributionEntry.class);
  private TerminologyClient terminologyClient;
  private UcumService ucumService;

  public SpiaDistribution(File file, TerminologyClient terminologyClient, UcumService ucumService)
      throws IOException, ValidationException {
    zipFile = new ZipFile(file);
    this.terminologyClient = terminologyClient;
    this.ucumService = ucumService;
    validate();
    parseRefsets();
  }

  private InputStream getNamedEntryAsStream(DistributionEntry distributionEntry)
      throws IOException {
    ZipEntry entry = zipFile.getEntry(expectedEntries.get(distributionEntry));
    logger.info("Reading file: \"" + entry.getName() + "\"");
    return zipFile.getInputStream(entry);
  }

  private void validate() throws ValidationException {
    List<String> entryNames = zipFile.stream().map(ZipEntry::getName).collect(Collectors.toList());
    for (String expectedEntryName : expectedEntries.values()) {
      if (!entryNames.contains(expectedEntryName)) {
        throw new ValidationException(
            "Expected entry not found in zip archive: " + expectedEntryName);
      }
    }
  }

  private void parseRefsets() throws ValidationException, IOException {
    for (DistributionEntry entry : expectedEntries.keySet()) {
      InputStream inputStream = getNamedEntryAsStream(entry);
      Workbook workbook;
      try {
        workbook = WorkbookFactory.create(inputStream);
      } catch (IOException e) {
        throw new ValidationException(
            "Error reading entry from ZIP file: " + expectedEntries.get(entry), e);
      }
      Refset parsedRefset;
      try {
        //noinspection unchecked
        Constructor constructor = entry.getParsingClass()
            .getConstructor(Workbook.class, TerminologyClient.class, UcumService.class);
        parsedRefset = (Refset) constructor
            .newInstance(workbook, terminologyClient, ucumService);
      } catch (InvocationTargetException e) {
        throw new RuntimeException("Error instantiating reference set parser", e.getCause());
      } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
        throw new RuntimeException("Error instantiating reference set parser", e);
      }
      refsets.put(entry, parsedRefset);
    }
  }

  public Map<DistributionEntry, Refset> getRefsets() {
    return refsets;
  }

  public enum DistributionEntry {
    REQUESTING(RequestingRefset.class),
    CHEMICAL(ChemicalPathologyRefset.class),
    HAEMATOLOGY(HaematologyRefset.class),
    IMMUNOPATHOLOGY(ImmunopathologyRefset.class),
    MICROBIOLOGY_SEROLOGY_MOLECULAR(MicrobiologySerologyMolecularRefset.class),
    MICROBIOLOGY_ORGANISMS(MicrobiologySubsetOfOrganismsRefset.class),
    PREFERRED_UNITS(PreferredUnitsRefset.class);

    private final Class parsingClass;

    DistributionEntry(Class parsingClass) {
      this.parsingClass = parsingClass;
    }

    public Class getParsingClass() {
      return parsingClass;
    }
  }

}

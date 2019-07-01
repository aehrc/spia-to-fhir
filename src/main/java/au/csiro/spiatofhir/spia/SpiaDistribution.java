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

import au.csiro.spiatofhir.fhir.TerminologyClient;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
 * @author John Grimes
 */
public class SpiaDistribution {

  // Map to the files within the distribution that contain each reference set.
  private static final Map<DistributionEntry, String> expectedEntries = new HashMap<DistributionEntry, String>() {{
    put(DistributionEntry.REQUESTING,
        "RCPA - SPIA Requesting Pathology Terminology Reference Set v3.0.xlsx");
    put(DistributionEntry.CHEMICAL,
        "RCPA - SPIA Chemical Pathology Terminology Reference Set v3.0.xlsx");
    put(DistributionEntry.HAEMATOLOGY,
        "RCPA - SPIA Haematology Terminology Reference Set v3.0.xlsx");
    put(DistributionEntry.IMMUNOPATHOLOGY,
        "RCPA - SPIA Immunopathology Terminology Reference Set v3.0.xlsx");
    put(DistributionEntry.MICROBIOLOGY_SEROLOGY_MOLECULAR,
        "RCPA - SPIA Microbiology Serology Molecular Pathology Terminology Reference Set v3.0.xlsx");
    put(DistributionEntry.MICROBIOLOGY_ORGANISMS,
        "RCPA - SPIA Microbiology Subset of Organisms mapped to SNOMED CT v3.0.xlsx");
    put(DistributionEntry.PREFERRED_UNITS, "RCPA - SPIA Preferred units v1.0.xlsx");
  }};
  private static final Logger logger = LoggerFactory.getLogger(SpiaDistribution.class);
  private ZipFile zipFile;
  private TerminologyClient terminologyClient;
  private UcumService ucumService;

  public SpiaDistribution(File file, TerminologyClient terminologyClient, UcumService ucumService)
      throws IOException, ValidationException {
    zipFile = new ZipFile(file);
    this.terminologyClient = terminologyClient;
    this.ucumService = ucumService;
    validate();
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

  /**
   * Returns the specified reference set from within the distribution.
   */
  public Refset getRefset(DistributionEntry distributionEntry)
      throws ValidationException, IOException {
    InputStream inputStream = getNamedEntryAsStream(distributionEntry);
    Workbook workbook;
    try {
      workbook = WorkbookFactory.create(inputStream);
    } catch (IOException e) {
      throw new ValidationException("Invalid Excel workbook format - must be OOXML (2007-) format");
    }
    switch (distributionEntry) {
      case REQUESTING:
        return new RequestingRefset(workbook, terminologyClient);
      case CHEMICAL:
        return new ChemicalPathologyRefset(workbook, terminologyClient, ucumService);
      case MICROBIOLOGY_SEROLOGY_MOLECULAR:
        return new MicrobiologySerologyMolecularRefset(workbook, terminologyClient, ucumService);
      case MICROBIOLOGY_ORGANISMS:
        return new MicrobiologySubsetOfOrganismsRefset(workbook, terminologyClient);
      case HAEMATOLOGY:
        return new HaematologyRefset(workbook, terminologyClient, ucumService);
      case IMMUNOPATHOLOGY:
        return new ImmunopathologyRefset(workbook, terminologyClient, ucumService);
      case PREFERRED_UNITS:
        return new PreferredUnitsRefset(workbook, terminologyClient, ucumService);
      default:
        throw new RuntimeException("Entry not supported yet: " + distributionEntry.toString());
    }
  }

  public enum DistributionEntry {
    REQUESTING,
    CHEMICAL,
    HAEMATOLOGY,
    IMMUNOPATHOLOGY,
    MICROBIOLOGY_SEROLOGY_MOLECULAR,
    MICROBIOLOGY_ORGANISMS,
    PREFERRED_UNITS
  }

}

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
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author John Grimes
 */
public class MicrobiologySubsetOfOrganismsRefset extends Refset implements HasRefsetEntries {

  protected static final Logger logger = LoggerFactory
      .getLogger(MicrobiologySubsetOfOrganismsRefset.class);
  protected static final String[] expectedHeaders =
      {"RCPA Preferred Organism name", "Terminology binding (SNOMED CT-AU)", "Version", "History",
          ""};
  private static final String SHEET_NAME = "Organisms mapped to SNOMED";
  private final Workbook workbook;
  private final TerminologyClient terminologyClient;
  private List<RefsetEntry> refsetEntries;

  /**
   * Creates a new reference set, based on the contents of the supplied workbook.
   */
  public MicrobiologySubsetOfOrganismsRefset(Workbook workbook, TerminologyClient terminologyClient)
      throws ValidationException {
    this.workbook = workbook;
    this.terminologyClient = terminologyClient;
    parse();
  }

  /**
   * Gets a list of all entries within this reference set.
   */
  @Override
  public List<RefsetEntry> getRefsetEntries() {
    return refsetEntries;
  }

  private void parse() throws ValidationException {
    Sheet sheet = workbook.getSheet(SHEET_NAME);
    refsetEntries = new ArrayList<>();
    for (Row row : sheet) {
      // Check that header row matches expectations.
      if (row.getRowNum() == 0) {
        validateHeaderRow(row, expectedHeaders);
        continue;
      }

      try {
        SnomedRefsetEntry refsetEntry = new SnomedRefsetEntry();

        // Extract information from row.
        String rcpaPreferredTerm = getStringValueFromCell(row, 0);
        String snomedCode = getSnomedCodeFromCell(row, 1, terminologyClient);
        Double version = getNumericValueFromCell(row, 2);
        String history = getStringValueFromCell(row, 3);

        // Populate information into ChemicalPathologyRefsetEntry object.
        refsetEntry.setRcpaPreferredTerm(rcpaPreferredTerm);
        refsetEntry.setSnomedCode(snomedCode);
        refsetEntry.setVersion(version);
        refsetEntry.setHistory(history);

        // Add SnomedRefsetEntry object to list.
        refsetEntries.add(refsetEntry);
      } catch (BlankCodeException e) {
      } catch (InvalidCodeException e) {
        // Skip any row which contains an invalid SNOMED code. A warning log message will be emitted.
        logger.warn(e.getMessage());
      }
    }
    // Lookup and add native display terms to reference set entries.
    List<String> preferredTerms = lookupDisplayTerms(terminologyClient,
        "http://snomed.info/sct",
        refsetEntries);
    for (int i = 0; i < refsetEntries.size(); i++) {
      SnomedRefsetEntry refsetEntry = (SnomedRefsetEntry) refsetEntries.get(i);
      if (preferredTerms.get(i) != null) {
        refsetEntry.setSnomedPreferredTerm(preferredTerms.get(i));
      }
    }
  }

}

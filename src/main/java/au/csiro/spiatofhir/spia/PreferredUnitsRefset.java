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
import org.fhir.ucum.UcumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author John Grimes
 */
public class PreferredUnitsRefset extends Refset implements HasRefsetEntries {

  protected static final Logger logger = LoggerFactory.getLogger(PreferredUnitsRefset.class);
  protected static final String[] expectedHeaders = {"Description", "Preferred Display ",
      "UCUM Unit"};
  private static final String SHEET_NAME = "Preferred units display";
  private final Workbook workbook;
  private final TerminologyClient terminologyClient;
  private final UcumService ucumService;
  private List<RefsetEntry> refsetEntries;

  public PreferredUnitsRefset(Workbook workbook, TerminologyClient terminologyClient,
      UcumService ucumService)
      throws ValidationException {
    this.workbook = workbook;
    this.terminologyClient = terminologyClient;
    this.ucumService = ucumService;
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
        UcumRefsetEntry refsetEntry = new UcumRefsetEntry();

        // Extract information from row.
        String description = getStringValueFromCell(row, 0);
        String rcpaPreferredTerm = getStringValueFromCell(row, 1);
        String ucumCode = getUcumCodeFromCell(ucumService, row, 2);

        // Populate information into UcumRefsetEntry object.
        refsetEntry.setDescription(description);
        refsetEntry.setRcpaPreferredTerm(rcpaPreferredTerm);
        refsetEntry.setUcumCode(ucumCode);

        // Add UcumRefsetEntry object to list.
        refsetEntries.add(refsetEntry);
      } catch (BlankCodeException e) {
      } catch (InvalidCodeException e) {
        // Skip any row which contains an invalid UCUM code. A warning log message will be emitted.
        logger.warn(e.getMessage());
      }
    }
    // Lookup and add native display terms to reference set entries.
    List<String> preferredTerms = lookupDisplayTerms(terminologyClient, "http://unitsofmeasure.org",
        refsetEntries);
    for (int i = 0; i < refsetEntries.size(); i++) {
      UcumRefsetEntry refsetEntry = (UcumRefsetEntry) refsetEntries.get(i);
      if (preferredTerms.get(i) != null) {
        refsetEntry.setUcumDisplay(preferredTerms.get(i));
      }
    }
  }

}

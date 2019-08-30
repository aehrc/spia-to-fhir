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
import au.csiro.spiatofhir.spia.RefsetEntry.CombiningResultsFlag;
import java.util.ArrayList;
import java.util.Set;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.fhir.ucum.UcumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author John Grimes
 */
public class ChemicalPathologyRefset extends Refset {

  protected static final Logger logger = LoggerFactory.getLogger(ChemicalPathologyRefset.class);
  protected static final String[] expectedHeaders =
      {"RCPA Preferred term", "RCPA Synonyms", "Usage guidance", "Subgroup_1", "Subgroup_2",
          "Length", "Specimen", "Unit", "UCUM", "LOINC", "Component", "Property", "Timing",
          "System", "Scale", "Method", "LongName", "Combining Results Flag", "Version", "History"};
  private static final String SHEET_NAME = "Chemical Pathology Terms v3.1";

  public ChemicalPathologyRefset(Workbook workbook,
      TerminologyClient terminologyClient, UcumService ucumService) throws ValidationException {
    super(workbook, terminologyClient, ucumService);
  }

  @Override
  protected void parse() throws ValidationException {
    Sheet sheet = workbook.getSheet(SHEET_NAME);
    if (sheet == null) {
      throw new ValidationException("Sheet not found: " + SHEET_NAME);
    }
    refsetEntries = new ArrayList<>();
    for (Row row : sheet) {
      // Check that header row matches expectations.
      if (row.getRowNum() == 0) {
        validateHeaderRow(row, expectedHeaders);
        continue;
      }
      // Skip "Therapeutic Drugs" header row.
      if (row.getRowNum() == 203) {
        continue;
      }

      RefsetEntry refsetEntry = new RefsetEntry();

      String rcpaPreferredTerm = getStringValueFromCell(row, 0);
      Set<String> rcpaSynonyms = getDelimitedStringsFromCell(row, 1);
      CombiningResultsFlag combiningResultsFlag = getCombiningResultsFlagFromCell(row, 17);
      String ucumCode = null, loincCode;

      // Skip entire row if code is missing or invalid.
      try {
        loincCode = getLoincCodeFromCell(row, 9, terminologyClient);
      } catch (BlankCodeException | InvalidCodeException e) {
        logger.warn(e.getMessage());
        continue;
      }

      // Warn if unit is missing or invalid.
      try {
        ucumCode = getUcumCodeFromCell(ucumService, row, 8);
      } catch (BlankCodeException | InvalidCodeException e) {
        logger.warn(e.getMessage());
      }

      // Populate information into RefsetEntry object.
      refsetEntry.setRcpaPreferredTerm(rcpaPreferredTerm);
      refsetEntry.setRcpaSynonyms(rcpaSynonyms);
      refsetEntry.setUnitCode(ucumCode);
      refsetEntry.setCode(loincCode);
      refsetEntry.setCombiningResultsFlag(combiningResultsFlag);

      // Add RefsetEntry object to list.
      refsetEntries.add(refsetEntry);
    }
  }


}

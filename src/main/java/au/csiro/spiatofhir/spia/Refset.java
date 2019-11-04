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

import static au.csiro.spiatofhir.spia.ValidationException.messageWithCoords;

import au.csiro.spiatofhir.fhir.TerminologyClient;
import au.csiro.spiatofhir.loinc.LoincCodeValidator;
import au.csiro.spiatofhir.snomed.SnomedCodeValidator;
import au.csiro.spiatofhir.spia.RefsetEntry.CombiningResultsFlag;
import au.csiro.spiatofhir.utils.Strings;
import java.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.fhir.ucum.UcumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used for parsing a SPIA reference set from a specified workbook within the distribution.
 * Validates terminology using a FHIR terminology service and a UCUM service.
 *
 * @author John Grimes
 */
public abstract class Refset {

  private static final Logger logger = LoggerFactory.getLogger(Refset.class);
  private static final String MULTI_VALUE_DELIMITER = ";";
  private static final Map<String, CombiningResultsFlag> combiningResultsFlagMap =
      new HashMap<String, CombiningResultsFlag>() {{
        put("Red", CombiningResultsFlag.RED);
        put("Green", CombiningResultsFlag.GREEN);
        put("Orange", CombiningResultsFlag.ORANGE);
      }};
  protected final Workbook workbook;
  protected final TerminologyClient terminologyClient;
  protected final UcumService ucumService;
  protected List<RefsetEntry> refsetEntries;

  public Refset(Workbook workbook, TerminologyClient terminologyClient,
      UcumService ucumService) throws ValidationException {
    this.workbook = workbook;
    this.terminologyClient = terminologyClient;
    this.ucumService = ucumService;
    parse();
  }

  protected void parse() throws ValidationException {
  }

  public List<RefsetEntry> getRefsetEntries() {
    return refsetEntries;
  }

  /**
   * Throws an exception if the supplied spreadsheet row does not match the specified array of
   * expected headers.
   */
  protected void validateHeaderRow(Row row, String[] expectedHeaders) throws ValidationException {
    ArrayList<String> headerValues = new ArrayList<>();
    for (Cell cell : row) {
      String stringCellValue = cell.getStringCellValue();
      if (stringCellValue != null && !stringCellValue.equals("")) {
        headerValues.add(stringCellValue);
      }
    }
    if (!Arrays.equals(headerValues.toArray(), expectedHeaders)) {
      throw new ValidationException("Header values do not match expected values.");
    }
  }

  /**
   * Returns a string value from the specified cell within a row, and asserts that it actually is a
   * string.
   */
  protected String getStringValueFromCell(Row row, int cellNumber) throws ValidationException {
    Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
    if (cell == null) {
      return null;
    }
    if (cell.getCellType() != CellType.STRING) {
      throw new CellValidationException(
          "Cell identified for extraction of string value is not of string type, " +
              "actual type: " + cell.getCellType().toString(),
          cell.getRowIndex(), cell.getColumnIndex());
    }
    final String trimmedValue = Strings.trim(cell.getStringCellValue());
    if (!cell.getStringCellValue().equals(trimmedValue)) {
      String message =
          "Encountered cell with leading or trailing whitespace, \"" + cell.getStringCellValue()
              + "\"";
      logger.warn(messageWithCoords(message, cell.getRowIndex(), cell.getColumnIndex()));
    }
    return trimmedValue;
  }

  protected Set<String> getDelimitedStringsFromCell(Row row, int cellNumber)
      throws ValidationException {
    String rawValue = getStringValueFromCell(row, cellNumber);
    Set<String> delimitedStrings = new HashSet<>();
    if (rawValue != null) {
      Arrays.stream(rawValue.split(MULTI_VALUE_DELIMITER)).forEach(s -> {
        String trimmedValue = Strings.trim(s);
        if (!s.equals(trimmedValue)) {
          String message =
              "Encountered delimited value with leading or trailing whitespace, \"" + s + "\"";
          logger.warn(messageWithCoords(message, row.getRowNum(), cellNumber));
        }
        delimitedStrings.add(trimmedValue);
      });
    }
    return delimitedStrings;
  }

  /**
   * Returns a string value from the specified cell within a row, asserting that a valid (though not
   * necessarily existent) SNOMED CT identifier is within the content and trimming any extraneous
   * surrounding content, such as preferred term.
   */
  protected String getSnomedCodeFromCell(Row row, int cellNumber,
      TerminologyClient terminologyClient)
      throws ValidationException, InvalidCodeException, BlankCodeException {
    Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
    if (cell == null) {
      throw new BlankCodeException("Blank SNOMED code encountered", row.getRowNum(), cellNumber);
    }
    String cellValue = getStringValueFromCell(row, cellNumber).split("\\|")[0];
    cellValue = Strings.trim(cellValue);
    // Check for the validity of the SNOMED code.
    SnomedCodeValidator snomedCodeValidator = new SnomedCodeValidator(terminologyClient);
    if (!snomedCodeValidator.validate(cellValue)) {
      throw new InvalidCodeException("Invalid SNOMED code encountered: \"" + cellValue + "\"",
          cell.getRowIndex(),
          cell.getColumnIndex());
    }
    if (!snomedCodeValidator.checkActive(cellValue)) {
      throw new InvalidCodeException("Inactive SNOMED code encountered: \"" + cellValue + "\"",
          cell.getRowIndex(),
          cell.getColumnIndex());
    }
    return cellValue;
  }

  /**
   * Returns a string value from the specified cell within a row, asserting that it is a valid
   * (though not necessarily existent) LOINC code.
   */
  protected String getLoincCodeFromCell(Row row, int cellNumber,
      TerminologyClient terminologyClient)
      throws ValidationException, InvalidCodeException, BlankCodeException {
    Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
    if (cell == null) {
      throw new BlankCodeException("Blank LOINC code encountered", row.getRowNum(), cellNumber);
    }
    String cellValue = getStringValueFromCell(row, cellNumber);
    // Check for the validity of the LOINC code.
    LoincCodeValidator loincCodeValidator = new LoincCodeValidator(terminologyClient);
    if (!loincCodeValidator.validate(cellValue)) {
      throw new InvalidCodeException("Invalid LOINC code encountered: \"" + cellValue + "\"",
          cell.getRowIndex(),
          cell.getColumnIndex());
    }
    if (!loincCodeValidator.checkActive(cellValue)) {
      throw new InvalidCodeException("Inactive LOINC code encountered: \"" + cellValue + "\"",
          cell.getRowIndex(),
          cell.getColumnIndex());
    }
    return cellValue;
  }

  /**
   * Returns a string value from the specified cell within a row, asserting that it is a valid UCUM
   * expression.
   */
  protected Set<String> getUcumCodesFromCell(UcumService ucumService, Row row, int cellNumber)
      throws BlankCodeException, ValidationException, InvalidCodeException {
    Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
    if (cell == null) {
      throw new BlankCodeException("Blank UCUM code encountered", row.getRowNum(), cellNumber);
    }
    if (cell.getCellType() != CellType.STRING) {
      throw new CellValidationException(
          "Cell identified for extraction of UCUM code is not of string type, actual type: " +
              cell.getCellType().toString(), cell.getRowIndex(), cell.getColumnIndex());
    }
    // Unit cells can contain multiple units.
    Set<String> cellValues = getDelimitedStringsFromCell(row, cellNumber);
    Set<String> results = new HashSet<>();
    for (String cellValue : cellValues) {
      if (cellValue.equals("No unit")) {
        return new HashSet<>();
      }
      // Check for the validity of the UCUM code. One invalid code within the cell will forfeit all
      // codes within the cell.
      String result = ucumService.validate(cellValue);
      if (result != null) {
        throw new InvalidCodeException("UCUM code validation failed: \"" + result + "\"",
            cell.getRowIndex(),
            cell.getColumnIndex());
      }
      results.add(cellValue);
    }
    return results;
  }

  protected CombiningResultsFlag getCombiningResultsFlagFromCell(Row row,
      int cellNumber)
      throws ValidationException {
    Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
    if (cell == null) {
      return null;
    }
    if (cell.getCellType() != CellType.STRING) {
      throw new CellValidationException(
          "Cell identified for extraction of Combining Results Flag is not of string type, "
              + "actual type: " + cell.getCellType().toString(), cell.getRowIndex(),
          cell.getColumnIndex());
    }
    if (!combiningResultsFlagMap.containsKey(cell.getStringCellValue())) {
      throw new ValidationException(
          "Unexpected value encountered in Combining Results Flag column: " + cell
              .getStringCellValue());
    }
    return combiningResultsFlagMap.get(cell.getStringCellValue());
  }

}

/*
 *    Copyright 2019 Australian e-Health Research Centre, CSIRO
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

package au.csiro.spiatofhir.spia;

import au.csiro.spiatofhir.fhir.TerminologyClient;
import au.csiro.spiatofhir.loinc.LoincCodeValidator;
import au.csiro.spiatofhir.snomed.SnomedCodeValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author John Grimes
 */
public abstract class Refset {

    private static final Logger logger = LoggerFactory.getLogger(Refset.class);

    /**
     * Returns a list of display terms corresponding to the supplied list of reference set entries, sourced using the
     * supplied terminology server client.
     * <p>
     * If there are any problems looking up a particular code, a null will be added to the list that is returned.
     */
    protected static List<String> lookupDisplayTerms(TerminologyClient terminologyClient, String system,
                                                     List<RefsetEntry> refsetEntries) {
        // Get the codes from the reference set entries.
        List<String> codes = refsetEntries.stream().map(RefsetEntry::getCode).collect(Collectors.toList());
        // Perform a lookup on each code using the terminology server.
        Bundle result = terminologyClient.batchLookup(system, codes, new ArrayList<>());
        // Return a list of displays.
        return result.getEntry()
                     .stream()
                     .map(Refset::entryToParameters)
                     .map(Refset::parametersToDisplayValue)
                     .collect(Collectors.toList());
    }

    /**
     * Uses the supplied terminology server client to look up display terms for LOINC reference set entries that have
     * UCUM codes.
     */
    protected static void addUcumDisplays(TerminologyClient terminologyClient,
                                          List<RefsetEntry> refsetEntries) {
        // Get the UCUM codes from the reference set entries.
        List<String> codes = refsetEntries.stream()
                                          .map(refsetEntry -> ((LoincRefsetEntry) refsetEntry).getUcumCode())
                                          .collect(Collectors.toList());
        // Perform a lookup on each code using the terminology server.
        Bundle result = terminologyClient.batchLookup("http://unitsofmeasure.org", codes, new ArrayList<>());
        // Get the display term from each lookup result.
        List<String> displays = result.getEntry()
                                      .stream()
                                      .map(Refset::entryToParameters)
                                      .map(Refset::parametersToDisplayValue)
                                      .collect(Collectors.toList());
        // Set the UCUM display to the authoritative display from the terminology server.
        for (int i = 0; i < refsetEntries.size(); i++) {
            LoincRefsetEntry refsetEntry = (LoincRefsetEntry) refsetEntries.get(i);
            if (displays.get(i) != null) refsetEntry.setUcumDisplay(displays.get(i));
        }
    }

    private static Parameters entryToParameters(Bundle.BundleEntryComponent entry) {
        if (entry.getResource().fhirType().equals("Parameters")) return (Parameters) entry.getResource();
        else if (entry.getResource().fhirType().equals("OperationOutcome")) {
            OperationOutcome operationOutcome = (OperationOutcome) entry.getResource();
            String opOutcomeMessage = operationOutcome.getIssueFirstRep().getDiagnostics();
            // Filter out log warnings for outcomes relating to blank unit codes.
            if (!opOutcomeMessage.matches("Invalid type for 'code' parameter\\.")) {
                String message = operationOutcome.getIssueFirstRep()
                                                 .getDiagnostics()
                                                 .replaceFirst("\\[[a-f0-9\\-]*\\]: ", "");
                logger.warn("Error looking up display for code: \"" + message + "\"");
            }
        }
        return null;
    }

    private static String parametersToDisplayValue(Parameters parameters) {
        return parameters != null ? parameters.getParameter()
                                              .stream()
                                              .filter(parameter -> parameter.getName().equals("display"))
                                              .map(parameter -> parameter.getValue().toString())
                                              .findFirst()
                                              .orElse(null) : null;
    }

    /**
     * Throws an exception if the supplied spreadsheet row does not match the specified array of expected headers.
     */
    protected void validateHeaderRow(Row row, String[] expectedHeaders) throws ValidationException {
        ArrayList<String> headerValues = new ArrayList<>();
        for (Cell cell : row) {
            headerValues.add(cell.getStringCellValue());
        }
        if (!Arrays.equals(headerValues.toArray(), expectedHeaders))
            throw new ValidationException("Header values do not match expected values.");
    }

    /**
     * Returns a string value from the specified cell within a row, and asserts that it actually is a string.
     */
    protected String getStringValueFromCell(Row row, int cellNumber) throws ValidationException {
        Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        if (cell.getCellTypeEnum() != CellType.STRING)
            throw new CellValidationException("Cell identified for extraction of string value is not of string type, " +
                                                      "actual type: " + cell.getCellTypeEnum().toString(),
                                              cell.getRowIndex(), cell.getColumnIndex());
        return cell.getStringCellValue().trim();
    }

    /**
     * Returns a numeric value from the specified cell within a row, and asserts that it actually is numeric.
     */
    protected Double getNumericValueFromCell(Row row, int cellNumber) throws ValidationException {
        Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        if (cell.getCellTypeEnum() != CellType.NUMERIC)
            throw new CellValidationException("Cell identified for extraction of numeric value is not of numeric " +
                                                      "type, actual type: " + cell.getCellTypeEnum().toString(),
                                              cell.getRowIndex(), cell.getColumnIndex());
        double value = cell.getNumericCellValue();
        if (value == 0) return null;
        else return value;
    }

    /**
     * Returns a string value from the specified cell within a row, asserting that a valid (though not necessarily
     * existent) SNOMED CT identifier is within the content and trimming any extraneous surrounding content, such as
     * preferred term.
     */
    protected String getSnomedCodeFromCell(Row row, int cellNumber)
            throws CellValidationException, InvalidCodeException, BlankCodeException {
        Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null)
            throw new BlankCodeException("Blank SNOMED code encountered", row.getRowNum(), cellNumber);
        if (cell.getCellTypeEnum() != CellType.STRING)
            throw new CellValidationException(
                    "Cell identified for extraction of SNOMED code is not of string type, actual type: " +
                            cell.getCellTypeEnum().toString(), cell.getRowIndex(), cell.getColumnIndex());
        String cellValue = cell.getStringCellValue()
                               .split("\\|")[0].trim();
        // Check for the validity of the SNOMED code.
        SnomedCodeValidator snomedCodeValidator = new SnomedCodeValidator();
        if (!snomedCodeValidator.validate(cellValue))
            throw new InvalidCodeException("Invalid SNOMED code encountered: \"" + cellValue + "\"",
                                           cell.getRowIndex(),
                                           cell.getColumnIndex());
        return cellValue;
    }

    /**
     * Returns a string value from the specified cell within a row, asserting that it is a valid (though not
     * necessarily existent) LOINC code.
     */
    protected String getLoincCodeFromCell(Row row, int cellNumber)
            throws CellValidationException, InvalidCodeException, BlankCodeException {
        Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null)
            throw new BlankCodeException("Blank LOINC code encountered", row.getRowNum(), cellNumber);
        if (cell.getCellTypeEnum() != CellType.STRING)
            throw new CellValidationException(
                    "Cell identified for extraction of LOINC code is not of string type, actual type: " +
                            cell.getCellTypeEnum().toString(), cell.getRowIndex(), cell.getColumnIndex());
        String cellValue = cell.getStringCellValue().trim();
        // Check for the validity of the LOINC code.
        LoincCodeValidator loincCodeValidator = new LoincCodeValidator();
        if (!loincCodeValidator.validate(cellValue))
            throw new InvalidCodeException("Invalid LOINC code encountered: \"" + cellValue + "\"",
                                           cell.getRowIndex(),
                                           cell.getColumnIndex());
        return cellValue;
    }

    /**
     * Returns a string value from the specified cell within a row, asserting that it is a valid UCUM expression.
     */
    protected String getUcumCodeFromCell(UcumService ucumService, Row row, int cellNumber)
            throws BlankCodeException, CellValidationException, InvalidCodeException {
        Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null)
            throw new BlankCodeException("Blank UCUM code encountered", row.getRowNum(), cellNumber);
        if (cell.getCellTypeEnum() != CellType.STRING)
            throw new CellValidationException(
                    "Cell identified for extraction of UCUM code is not of string type, actual type: " +
                            cell.getCellTypeEnum().toString(), cell.getRowIndex(), cell.getColumnIndex());
        String cellValue = cell.getStringCellValue().trim();
        // Check for the validity of the UCUM code.
        String result = ucumService.validate(cellValue);
        if (result != null)
            throw new InvalidCodeException("UCUM code validation failed: \"" + result + "\"",
                                           cell.getRowIndex(),
                                           cell.getColumnIndex());
        return cellValue;
    }

}

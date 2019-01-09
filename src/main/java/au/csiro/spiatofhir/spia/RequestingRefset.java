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

package au.csiro.spiatofhir.spia;

import au.csiro.spiatofhir.fhir.TerminologyClient;
import au.csiro.spiatofhir.snomed.SnomedCodeValidator;
import org.apache.poi.ss.usermodel.*;

import java.util.*;

/**
 * @author John Grimes
 */
public class RequestingRefset extends Refset implements HasRefsetEntries {

    protected static final String[] expectedHeaders =
            {"RCPA Preferred term", "RCPA Synonyms", "Usage guidance", "Length", "Specimen",
             "Terminology binding (SNOMED CT-AU)", "Version", "History"};
    private static final String SHEET_NAME = "Terminology for Requesting Path";
    private final Workbook workbook;
    private final TerminologyClient terminologyClient;
    private final SnomedCodeValidator snomedCodeValidator;
    private List<RefsetEntry> refsetEntries;

    /**
     * Creates a new reference set, based on the contents of the supplied workbook.
     */
    public RequestingRefset(Workbook workbook, TerminologyClient terminologyClient) throws ValidationException {
        this.workbook = workbook;
        this.terminologyClient = terminologyClient;
        snomedCodeValidator = new SnomedCodeValidator();
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

            SnomedRefsetEntry refsetEntry = new SnomedRefsetEntry();

            // Extract information from row.
            String rcpaPreferredTerm = getStringValueFromCell(row, 0);
            String rcpaSynonymsRaw = getStringValueFromCell(row, 1);
            Set<String> rcpaSynonyms = new HashSet<>();
            if (rcpaSynonymsRaw != null) {
                Arrays.stream(rcpaSynonymsRaw.split(";")).forEach(s -> rcpaSynonyms.add(s.trim()));
            }
            String usageGuidance = getStringValueFromCell(row, 2);
            // Length has been omitted, as formulas are being used within the spreadsheet.
            String specimen = getStringValueFromCell(row, 4);
            String snomedCode = getSnomedCodeFromCell(row, 5);
            // Skip whole row unless there is a valid SNOMED code.
            if (snomedCode == null || !snomedCodeValidator.validate(snomedCode)) continue;
            Double version = getNumericValueFromCell(row, 6);
            String history = getStringValueFromCell(row, 7);

            // Populate information into SnomedRefsetEntry object.
            refsetEntry.setRcpaPreferredTerm(rcpaPreferredTerm);
            refsetEntry.setRcpaSynonyms(rcpaSynonyms);
            refsetEntry.setUsageGuidance(usageGuidance);
            refsetEntry.setSpecimen(specimen);
            refsetEntry.setSnomedCode(snomedCode);
            refsetEntry.setVersion(version);
            refsetEntry.setHistory(history);

            // Add LoincRefsetEntry object to list.
            refsetEntries.add(refsetEntry);
        }
        // Lookup and add native display terms to reference set entries.
        List<String> preferredTerms = lookupDisplayTerms(terminologyClient, "http://snomed.info/sct", refsetEntries);
        for (int i = 0; i < refsetEntries.size(); i++) {
            SnomedRefsetEntry refsetEntry = (SnomedRefsetEntry) refsetEntries.get(i);
            if (preferredTerms.get(i) != null) refsetEntry.setSnomedPreferredTerm(preferredTerms.get(i));
        }
    }

    private String getSnomedCodeFromCell(Row row, int cellNumber) throws CellValidationException {
        Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        if (cell.getCellTypeEnum() != CellType.STRING)
            throw new CellValidationException(
                    "Cell identified for extraction of SNOMED code is not of string type, actual type: " +
                            cell.getCellTypeEnum().toString(), cell.getRowIndex(), cell.getColumnIndex());
        String cellValue = cell.getStringCellValue();
        return cellValue.split("\\|")[0].trim();
    }

}

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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

/**
 * @author John Grimes
 */
public class ImmunopathologyRefset extends Refset implements HasRefsetEntries {

    protected static final String[] expectedHeaders =
            {"RCPA Preferred term", "RCPA Synonyms", "Usage guidance", "Length", "Specimen", "Unit", "UCUM", "LOINC",
             "Component", "Property", "Timing", "System", "Scale", "Method", "LongName", "Version", "History"};
    private static final String SHEET_NAME = "Terminology for Immunopathology";
    private Workbook workbook;
    private Sheet sheet;
    private List<RefsetEntry> refsetEntries;

    /**
     * Creates a new reference set, based on the contents of the supplied workbook.
     */
    public ImmunopathologyRefset(Workbook workbook) throws ValidationException {
        this.workbook = workbook;
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
        sheet = workbook.getSheet(SHEET_NAME);
        refsetEntries = new ArrayList<>();
        for (Row row : sheet) {
            // Check that header row matches expectations.
            if (row.getRowNum() == 0) {
                validateHeaderRow(row, expectedHeaders);
                continue;
            }
            // Skip heading rows
            int[] headingRows = {1, 88, 132, 133, 142, 154, 166, 180, 191};
            if (Arrays.asList(headingRows).contains(row.getRowNum())) continue;

            LoincRefsetEntry refsetEntry = new LoincRefsetEntry();

            // Extract information from row.
            Optional<String> rcpaPreferredTerm = getStringValueFromCell(row, 0);
            Optional<String> rcpaSynonymsRaw = getStringValueFromCell(row, 1);
            Set<String> rcpaSynonyms = new HashSet<>();
            if (rcpaSynonymsRaw.isPresent()) {
                Arrays.asList(rcpaSynonymsRaw.get().split(";")).stream().forEach(s -> rcpaSynonyms.add(s.trim()));
            }
            Optional<String> usageGuidance = getStringValueFromCell(row, 2);
            // Length has been omitted, as formulas are being used within the spreadsheet.
            Optional<String> specimen = getStringValueFromCell(row, 4);
            Optional<String> unit = getStringValueFromCell(row, 5);
            Optional<String> ucum = getStringValueFromCell(row, 6);
            Optional<String> loincCode = getStringValueFromCell(row, 7);
            Optional<String> loincComponent = getStringValueFromCell(row, 8);
            Optional<String> loincProperty = getStringValueFromCell(row, 9);
            Optional<String> loincTiming = getStringValueFromCell(row, 10);
            Optional<String> loincSystem = getStringValueFromCell(row, 11);
            Optional<String> loincScale = getStringValueFromCell(row, 12);
            Optional<String> loincMethod = getStringValueFromCell(row, 13);
            Optional<String> loincLongName = getStringValueFromCell(row, 14);
            Optional<Double> version = getNumericValueFromCell(row, 15);
            Optional<String> history = getStringValueFromCell(row, 16);

            // Populate information into LoincRefsetEntry object.
            refsetEntry.setRcpaPreferredTerm(rcpaPreferredTerm);
            refsetEntry.setRcpaSynonyms(rcpaSynonyms);
            refsetEntry.setUsageGuidance(usageGuidance);
            refsetEntry.setSpecimen(specimen);
            refsetEntry.setUnit(unit);
            refsetEntry.setUcum(ucum);
            refsetEntry.setCode(loincCode);
            refsetEntry.setLoincComponent(loincComponent);
            refsetEntry.setLoincProperty(loincProperty);
            refsetEntry.setLoincTiming(loincTiming);
            refsetEntry.setLoincSystem(loincSystem);
            refsetEntry.setLoincScale(loincScale);
            refsetEntry.setLoincMethod(loincMethod);
            refsetEntry.setLoincLongName(loincLongName);
            refsetEntry.setVersion(version);
            refsetEntry.setHistory(history);

            // Add LoincRefsetEntry object to list.
            refsetEntries.add(refsetEntry);
        }
    }

}

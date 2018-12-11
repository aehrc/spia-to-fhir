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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author John Grimes
 */
public class PreferredUnitsRefset extends Refset implements HasRefsetEntries {

    protected static final String[] expectedHeaders = {"Description", "Preferred Display ", "UCUM Unit"};
    private static final String SHEET_NAME = "Preferred units display";
    private Workbook workbook;
    private Sheet sheet;
    private List<RefsetEntry> refsetEntries;

    public PreferredUnitsRefset(Workbook workbook) throws ValidationException {
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
        refsetEntries = new ArrayList<RefsetEntry>();
        for (Row row : sheet) {
            // Check that header row matches expectations.
            if (row.getRowNum() == 0) {
                validateHeaderRow(row, expectedHeaders);
                continue;
            }

            UcumRefsetEntry refsetEntry = new UcumRefsetEntry();

            // Extract information from row.
            Optional<String> description = getStringValueFromCell(row, 0);
            Optional<String> rcpaPreferredTerm = getStringValueFromCell(row, 1);
            Optional<String> ucumCode = getStringValueFromCell(row, 2);

            // Populate information into UcumRefsetEntry object.
            refsetEntry.setDescription(description);
            refsetEntry.setRcpaPreferredTerm(rcpaPreferredTerm);
            refsetEntry.setCode(ucumCode);

            // Add UcumRefsetEntry object to list.
            refsetEntries.add(refsetEntry);
        }
    }

}

/*
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
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
     *
     * @return
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

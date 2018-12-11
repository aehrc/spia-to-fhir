/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.spia;

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
    private Workbook workbook;
    private Sheet sheet;
    private List<RefsetEntry> refsetEntries;

    /**
     * Creates a new reference set, based on the contents of the supplied workbook.
     */
    public RequestingRefset(Workbook workbook) throws ValidationException {
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

            SnomedRefsetEntry refsetEntry = new SnomedRefsetEntry();

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
            Optional<String> snomedCode = getSnomedCodeFromCell(row, 5);
            Optional<Double> version = getNumericValueFromCell(row, 6);
            Optional<String> history = getStringValueFromCell(row, 7);

            // Populate information into SnomedRefsetEntry object.
            refsetEntry.setRcpaPreferredTerm(rcpaPreferredTerm);
            refsetEntry.setRcpaSynonyms(rcpaSynonyms);
            refsetEntry.setUsageGuidance(usageGuidance);
            refsetEntry.setSpecimen(specimen);
            refsetEntry.setCode(snomedCode);
            refsetEntry.setVersion(version);
            refsetEntry.setHistory(history);

            // Add LoincRefsetEntry object to list.
            refsetEntries.add(refsetEntry);
        }
    }

    private Optional<String> getSnomedCodeFromCell(Row row, int cellNumber) throws CellValidationException {
        Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return Optional.empty();
        if (cell.getCellTypeEnum() != CellType.STRING)
            throw new CellValidationException(
                    "Cell identified for extraction of SNOMED code is not of string type, actual type: " +
                            cell.getCellTypeEnum().toString(), cell.getRowIndex(), cell.getColumnIndex());
        String cellValue = cell.getStringCellValue();
        String code = cellValue.split("\\|")[0].trim();
        return Optional.of(code);
    }

}

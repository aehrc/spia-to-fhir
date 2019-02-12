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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.fhir.ucum.UcumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author John Grimes
 */
public class HaematologyRefset extends Refset implements HasRefsetEntries {

    protected static final Logger logger = LoggerFactory.getLogger(HaematologyRefset.class);
    protected static final String[] expectedHeaders =
            {"RCPA Preferred term", "RCPA Synonyms", "Usage guidance", "Length", "Specimen", "Unit", "UCUM", "LOINC",
                    "Component", "Property", "Timing", "System", "Scale", "Method", "LongName", "Version", "History"};
    private static final String SHEET_NAME = "Terminology for Haematology";
    private final Workbook workbook;
    private final TerminologyClient terminologyClient;
    private final UcumService ucumService;
    private List<RefsetEntry> refsetEntries;

    /**
     * Creates a new reference set, based on the contents of the supplied workbook.
     */
    public HaematologyRefset(Workbook workbook, TerminologyClient terminologyClient, UcumService ucumService)
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
                LoincRefsetEntry refsetEntry = new LoincRefsetEntry();

                // Extract information from row.
                String rcpaPreferredTerm = getStringValueFromCell(row, 0);
                // Skip rows that have "Cross match" as the preferred term.
                if (rcpaPreferredTerm != null && rcpaPreferredTerm.equals("Cross match")) continue;
                String rcpaSynonymsRaw = getStringValueFromCell(row, 1);
                Set<String> rcpaSynonyms = new HashSet<>();
                if (rcpaSynonymsRaw != null) {
                    Arrays.stream(rcpaSynonymsRaw.split(";")).forEach(s -> rcpaSynonyms.add(s.trim()));
                }
                String usageGuidance = getStringValueFromCell(row, 2);
                // Length has been omitted, as formulas are being used within the spreadsheet.
                String specimen = getStringValueFromCell(row, 4);
                String rcpaUnit = getStringValueFromCell(row, 5);
                String ucumCode = null;
                try {
                    ucumCode = getUcumCodeFromCell(ucumService, row, 6);
                } catch (BlankCodeException e) {
                } catch (InvalidCodeException e) {
                    logger.warn(e.getMessage());
                }
                String loincCode = getLoincCodeFromCell(row, 7, terminologyClient);
                String loincComponent = getStringValueFromCell(row, 8);
                String loincProperty = getStringValueFromCell(row, 9);
                String loincTiming = getStringValueFromCell(row, 10);
                String loincSystem = getStringValueFromCell(row, 11);
                String loincScale = getStringValueFromCell(row, 12);
                String loincMethod = getStringValueFromCell(row, 13);
                String loincLongName = getStringValueFromCell(row, 14);
                Double version = getNumericValueFromCell(row, 15);
                String history = getStringValueFromCell(row, 16);

                // Populate information into LoincRefsetEntry object.
                refsetEntry.setRcpaPreferredTerm(rcpaPreferredTerm);
                refsetEntry.setRcpaSynonyms(rcpaSynonyms);
                refsetEntry.setUsageGuidance(usageGuidance);
                refsetEntry.setSpecimen(specimen);
                refsetEntry.setRcpaUnit(rcpaUnit);
                refsetEntry.setUcumCode(ucumCode);
                refsetEntry.setLoincCode(loincCode);
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
            } catch (BlankCodeException e) {
            } catch (InvalidCodeException e) {
                // Skip any row which contains an invalid LOINC code. A warning log message will be emitted.
                logger.warn(e.getMessage());
            }
        }
        // Lookup and add native display terms to reference set entries.
        List<String> preferredTerms = lookupDisplayTerms(terminologyClient, "http://loinc.org", refsetEntries);
        for (int i = 0; i < refsetEntries.size(); i++) {
            LoincRefsetEntry refsetEntry = (LoincRefsetEntry) refsetEntries.get(i);
            if (preferredTerms.get(i) != null) refsetEntry.setLoincLongName(preferredTerms.get(i));
        }
        addUcumDisplays(terminologyClient, refsetEntries);
    }

}

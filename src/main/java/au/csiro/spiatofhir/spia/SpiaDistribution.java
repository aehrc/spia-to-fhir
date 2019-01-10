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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author John Grimes
 */
public class SpiaDistribution {

    private static final Map<DistributionEntry, String> expectedEntries =
            Map.of(
                    DistributionEntry.REQUESTING,
                    "RCPA - SPIA Requesting Pathology Terminology Reference Set v3.0.xlsx",
                    DistributionEntry.CHEMICAL,
                    "RCPA - SPIA Chemical Pathology Terminology Reference Set v3.0.xlsx",
                    DistributionEntry.HAEMATOLOGY,
                    "RCPA - SPIA Haematology Terminology Reference Set v3.0.xlsx",
                    DistributionEntry.IMMUNOPATHOLOGY,
                    "RCPA - SPIA Immunopathology Terminology Reference Set v3.0.xlsx",
                    DistributionEntry.MICROBIOLOGY_SEROLOGY_MOLECULAR,
                    "RCPA - SPIA Microbiology Serology Molecular Pathology Terminology Reference Set v3.0.xlsx",
                    DistributionEntry.MICROBIOLOGY_ORGANISMS,
                    "RCPA - SPIA Microbiology Subset of Organisms mapped to SNOMED CT v3.0.xlsx",
                    DistributionEntry.PREFERRED_UNITS,
                    "RCPA - SPIA Preferred units v1.0.xlsx"
            );
    private ZipFile zipFile;
    private TerminologyClient terminologyClient;

    public SpiaDistribution(File file, TerminologyClient terminologyClient) throws IOException, ValidationException {
        zipFile = new ZipFile(file);
        this.terminologyClient = terminologyClient;
        validate();
    }

    private InputStream getNamedEntryAsStream(DistributionEntry distributionEntry) throws IOException {
        ZipEntry entry = zipFile.getEntry(expectedEntries.get(distributionEntry));
        return zipFile.getInputStream(entry);
    }

    private void validate() throws ValidationException {
        List<String> entryNames = zipFile.stream().map(ZipEntry::getName).collect(Collectors.toList());
        for (String expectedEntryName : expectedEntries.values()) {
            if (!entryNames.contains(expectedEntryName))
                throw new ValidationException("Expected entry not found in zip archive: " + expectedEntryName);
        }
    }

    /**
     * Returns the specified reference set from within the distribution.
     */
    public Refset getRefset(DistributionEntry distributionEntry) throws ValidationException, IOException {
        InputStream inputStream = getNamedEntryAsStream(distributionEntry);
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException e) {
            throw new ValidationException("Invalid Excel workbook format - must be OOXML (2007-) format");
        }
        switch (distributionEntry) {
            case REQUESTING:
                return new RequestingRefset(workbook, terminologyClient);
            case CHEMICAL:
                return new ChemicalPathologyRefset(workbook, terminologyClient);
            case MICROBIOLOGY_SEROLOGY_MOLECULAR:
                return new MicrobiologySerologyMolecularRefset(workbook, terminologyClient);
            case MICROBIOLOGY_ORGANISMS:
                return new MicrobiologySubsetOfOrganismsRefset(workbook, terminologyClient);
            case HAEMATOLOGY:
                return new HaematologyRefset(workbook, terminologyClient);
            case IMMUNOPATHOLOGY:
                return new ImmunopathologyRefset(workbook, terminologyClient);
            case PREFERRED_UNITS:
                return new PreferredUnitsRefset(workbook, terminologyClient);
            default:
                throw new RuntimeException("Entry not supported yet: " + distributionEntry.toString());
        }
    }

    public enum DistributionEntry {
        REQUESTING,
        CHEMICAL,
        HAEMATOLOGY,
        IMMUNOPATHOLOGY,
        MICROBIOLOGY_SEROLOGY_MOLECULAR,
        MICROBIOLOGY_ORGANISMS,
        PREFERRED_UNITS
    }

}

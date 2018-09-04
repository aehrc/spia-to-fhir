/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.spia;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SpiaDistribution {

    private static final Map<DistributionEntry, String> expectedEntries =
            Collections.unmodifiableMap(new HashMap<DistributionEntry, String>() {
                {
                    put(DistributionEntry.CHEMICAL,
                        "RCPA - SPIA Chemical Pathology Terminology Reference Set v3.0.xlsx");
                    put(DistributionEntry.HAEMATOLOGY, "RCPA - SPIA Haematology Terminology Reference Set v3.0.xlsx");
                    put(DistributionEntry.IMMUNOPATHOLOGY,
                        "RCPA - SPIA Immunopathology Terminology Reference Set v3.0.xlsx");
                    put(DistributionEntry.MICROBIOLOGY_SEROLOGY_MOLECULAR,
                        "RCPA - SPIA Microbiology Serology Molecular Pathology Terminology Reference Set v3.0.xlsx");
                    put(DistributionEntry.PREFERRED_UNITS, "RCPA - SPIA Preferred units v1.0.xlsx");
                    put(DistributionEntry.REQUESTING,
                        "RCPA - SPIA Requesting Pathology Terminology Reference Set v3.0.xlsx");
                }
            });
    private MicrobiologySerologyMolecularRefset microbiologySerologyMolecularRefset;
    private ZipFile zipFile;

    public SpiaDistribution(File file) throws IOException, ValidationException {
        zipFile = new ZipFile(file);
        validate();
    }

    private InputStream getNamedEntryAsStream(DistributionEntry distributionEntry) throws IOException {
        ZipEntry entry = zipFile.getEntry(expectedEntries.get(distributionEntry));
        return zipFile.getInputStream(entry);
    }

    private void validate() throws ValidationException {
        List<String> entryNames = zipFile.stream().map(entry -> entry.getName()).collect(Collectors.toList());
        for (String expectedEntryName : expectedEntries.values()) {
            if (!entryNames.contains(expectedEntryName))
                throw new ValidationException("Expected entry not found in zip archive: " + expectedEntryName);
        }
    }

    /**
     * Returns the specified reference set from within the distribution.
     *
     * @param distributionEntry
     * @return
     * @throws ValidationException
     * @throws IOException
     */
    public HasRefsetEntries getRefset(DistributionEntry distributionEntry) throws ValidationException, IOException {
        InputStream inputStream = getNamedEntryAsStream(distributionEntry);
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException e) {
            throw new ValidationException("Invalid Excel workbook format - must be OOXML (2007-) format");
        }
        switch (distributionEntry) {
            case CHEMICAL:
                return new ChemicalPathologyRefset(workbook);
            case MICROBIOLOGY_SEROLOGY_MOLECULAR:
                return new MicrobiologySerologyMolecularRefset(workbook);
            case HAEMATOLOGY:
                return new HaematologyRefset(workbook);
            case IMMUNOPATHOLOGY:
                return new ImmunopathologyRefset(workbook);
            case REQUESTING:
                return new RequestingRefset(workbook);
            default:
                throw new RuntimeException("Entry not supported yet: " + distributionEntry.toString());
        }
    }

    public enum DistributionEntry {
        CHEMICAL,
        HAEMATOLOGY,
        IMMUNOPATHOLOGY,
        MICROBIOLOGY_SEROLOGY_MOLECULAR,
        PREFERRED_UNITS,
        REQUESTING
    }

}

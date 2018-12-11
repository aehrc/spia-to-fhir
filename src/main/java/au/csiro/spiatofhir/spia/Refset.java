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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author John Grimes
 */
public abstract class Refset {

    protected void validateHeaderRow(Row row, String[] expectedHeaders) throws ValidationException {
        ArrayList<String> headerValues = new ArrayList<>();
        for (Cell cell : row) {
            headerValues.add(cell.getStringCellValue());
        }
        if (!Arrays.equals(headerValues.toArray(), expectedHeaders))
            throw new ValidationException("Header values do not match expected values.");
    }

    protected Optional<String> getStringValueFromCell(Row row, int cellNumber) throws ValidationException {
        Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return Optional.empty();
        if (cell.getCellTypeEnum() != CellType.STRING)
            throw new CellValidationException("Cell identified for extraction of string value is not of string type, " +
                                                      "actual type: " + cell.getCellTypeEnum().toString(),
                                              cell.getRowIndex(), cell.getColumnIndex());
        return Optional.of(cell.getStringCellValue().trim());
    }

    protected Optional<Double> getNumericValueFromCell(Row row, int cellNumber) throws ValidationException {
        Cell cell = row.getCell(cellNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return Optional.empty();
        if (cell.getCellTypeEnum() != CellType.NUMERIC)
            throw new CellValidationException("Cell identified for extraction of numeric value is not of numeric " +
                                                      "type, actual type: " + cell.getCellTypeEnum().toString(),
                                              cell.getRowIndex(), cell.getColumnIndex());
        double value = cell.getNumericCellValue();
        if (value == 0) return Optional.empty();
        else return Optional.of(value);
    }

}

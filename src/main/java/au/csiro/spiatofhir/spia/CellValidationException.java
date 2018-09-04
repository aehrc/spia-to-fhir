/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.spia;

public class CellValidationException extends ValidationException {

    public CellValidationException(String message, int rowIndex, int columnIndex) {
        super(message + " (row: " + rowIndex + ", column: " + columnIndex + ")");
    }

}

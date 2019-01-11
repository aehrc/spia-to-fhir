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

/**
 * Thrown when encountering cells in spreadsheets that do not contain the expected data types.
 *
 * @author John Grimes
 */
public class CellValidationException extends ValidationException {

    public CellValidationException(String message, int rowIndex, int columnIndex) {
        super(message + " (row: " + rowIndex + ", column: " + columnIndex + ")");
    }

}

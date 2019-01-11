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

package au.csiro.spiatofhir.loinc;

import junit.framework.TestCase;

public class LoincCodeValidatorTest extends TestCase {

    private static final String[] validCodes = {"26924-1", "14682-9", "14933-6", "LL1001-8", "LP173485-6", "LA4108-2"};
    private static final String[] invalidCodes = {"dog", "26dog5", "26dog5-4", "26924-2", "LA4108-3", "{14682-9}"};
    private LoincCodeValidator loincCodeValidator;

    public void setUp() throws Exception {
        super.setUp();
        loincCodeValidator = new LoincCodeValidator();
    }

    public void testValidate() {
        for (String validCode : validCodes) {
            assertTrue("Expected valid code was not valid: " + validCode, loincCodeValidator.validate(validCode));
        }
        for (String invalidCode : invalidCodes) {
            assertFalse("Expected invalid code was valid: " + invalidCode, loincCodeValidator.validate(invalidCode));
        }
    }

}
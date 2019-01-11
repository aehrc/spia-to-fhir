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

package au.csiro.spiatofhir.snomed;

import junit.framework.TestCase;

/**
 * Test cases taken from https://github.com/AuDigitalHealth/polecat/blob/7d9eaeeb102842795582e322e7b14d9a12925f70/src
 * /snomed/sctid.test.js
 *
 * @author John Grimes
 */
public class SnomedCodeValidatorTest extends TestCase {

    private static final String[] validCodes =
            {"21433011000036107", "54316011000036102", "2594011000036109", "780701000168107", "21258011000036102"};
    private static final String[] invalidCodes =
            {"dog", "1234", "21433011000036106", "21258001000036102", "0438505003", "001256", "53919011000036101."};
    private SnomedCodeValidator snomedCodeValidator;

    public void setUp() throws Exception {
        super.setUp();
        snomedCodeValidator = new SnomedCodeValidator();
    }

    public void testValidate() {
        for (String validCode : validCodes) {
            assertTrue("Expected valid code was not valid: " + validCode, snomedCodeValidator.validate(validCode));
        }
        for (String invalidCode : invalidCodes) {
            assertFalse("Expected invalid code was valid: " + invalidCode, snomedCodeValidator.validate(invalidCode));
        }
    }

}
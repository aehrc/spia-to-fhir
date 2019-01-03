package au.csiro.spiatofhir.snomed;

import junit.framework.TestCase;

/**
 * Test cases taken from https://github.com/AuDigitalHealth/polecat/blob/7d9eaeeb102842795582e322e7b14d9a12925f70/src
 * /snomed/sctid.test.js
 *
 * @author John Grimes
 */
public class SnomedCodeValidatorTest extends TestCase {

    private static String[] validCodes =
            {"21433011000036107", "54316011000036102", "2594011000036109", "780701000168107", "21258011000036102"};
    private static String[] invalidCodes =
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
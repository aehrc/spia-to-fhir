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
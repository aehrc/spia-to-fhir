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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author John Grimes
 */
public class LoincCodeValidator {

    /**
     * Validates whether an input string is a valid LOINC code, in terms of its structure and the validity of its
     * check digit.
     */
    public boolean validate(String code) {
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)-(\\d)");
        Matcher matcher = pattern.matcher(code);
        if (!matcher.matches()) return false;
        String codeMinusCheckDigit = matcher.group(1);
        String checkDigit = matcher.group(2);
        int computedCheckDigit;
        try {
            computedCheckDigit = computeCheckDigit(codeMinusCheckDigit);
        } catch (Exception e) {
            return false;
        }
        return checkDigit.equals(Integer.toString(computedCheckDigit));
    }

    /**
     * Computes the check digit for the identifier portion of a LOINC code.
     * <p>
     * Taken from: https://wiki.openmrs.org/display/docs/Check+Digit+Algorithm
     */
    private int computeCheckDigit(String idWithoutCheckDigit) throws Exception {
        String validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVYWXZ_";
        idWithoutCheckDigit = idWithoutCheckDigit.trim().toUpperCase();
        int sum = 0;
        for (int i = 0; i < idWithoutCheckDigit.length(); i++) {
            char ch = idWithoutCheckDigit.charAt(idWithoutCheckDigit.length() - i - 1);
            if (validChars.indexOf(ch) == -1)
                throw new Exception("\"" + ch + "\" is an invalid character");
            int digit = (int) ch - 48;
            int weight;
            if (i % 2 == 0) {
                weight = (2 * digit) - (digit / 5) * 9;
            } else {
                weight = digit;
            }
            sum += weight;
        }
        sum = Math.abs(sum) + 10;
        return (10 - (sum % 10)) % 10;
    }

}

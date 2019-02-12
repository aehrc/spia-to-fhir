/*
 *    Copyright 2019 Australian e-Health Research Centre, CSIRO
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

import au.csiro.spiatofhir.fhir.TerminologyClient;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Parameters;

import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author John Grimes
 */
public class LoincCodeValidator {

    private TerminologyClient terminologyClient;

    public LoincCodeValidator() {
    }

    public LoincCodeValidator(TerminologyClient terminologyClient) {
        this.terminologyClient = terminologyClient;
    }

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


    public boolean checkActive(String code) {
        Parameters result = terminologyClient.lookup("http://loinc.org", code, Collections.singletonList("inactive"));
        if (result.getParameter() == null) return true;
        Optional<Parameters.ParametersParameterComponent> parameter = result.getParameter().stream()
                .filter(p -> {
                    boolean isProperty = p.getName().equals("property");
                    Optional<Parameters.ParametersParameterComponent> codePart = p.getPart().stream()
                            .filter(pp -> pp.getName().equals("code") && ((CodeType) pp.getValue()).asStringValue().equals("inactive"))
                            .findFirst();
                    Optional<Parameters.ParametersParameterComponent> valuePart = p.getPart().stream()
                            .filter(pp -> pp.getName().equals("valueBoolean") && ((BooleanType) pp.getValue()).booleanValue())
                            .findFirst();
                    return isProperty && codePart.isPresent() && valuePart.isPresent();
                })
                .findFirst();
        return parameter.isEmpty();
    }

}

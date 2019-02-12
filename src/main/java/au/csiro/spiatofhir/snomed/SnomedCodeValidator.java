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

package au.csiro.spiatofhir.snomed;

import au.csiro.spiatofhir.fhir.TerminologyClient;
import au.csiro.spiatofhir.utils.Verhoeff;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adapted from https://github.com/AuDigitalHealth/polecat/blob/7d9eaeeb102842795582e322e7b14d9a12925f70/src/snomed
 * /sctid.js
 *
 * @author John Grimes
 */
public class SnomedCodeValidator {

    private TerminologyClient terminologyClient;

    public SnomedCodeValidator() {
    }

    public SnomedCodeValidator(TerminologyClient terminologyClient) {
        this.terminologyClient = terminologyClient;
    }

    /**
     * Validates whether an input string is a valid SNOMED CT identifier, based on its structure and the validity of
     * its check digit.
     */
    public boolean validate(String code) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(code);
        if (!matcher.matches()) return false;
        long number;
        try {
            number = Long.parseLong(code, 10);
        } catch (NumberFormatException e) {
            return false;
        }
        if (number <= Math.pow(10, 5) || number > Math.pow(10, 18)) return false;
        String partitionId = code.substring(code.length() - 3, code.length() - 1);
        String[] validPartitions = {"00", "01", "02", "10", "11", "12"};
        if (!Arrays.asList(validPartitions).contains(partitionId)) return false;
        return Verhoeff.validateVerhoeff(code);
    }

    public boolean checkActive(String code) {
        Parameters result = terminologyClient.lookup("http://snomed.info/sct", code, Collections.singletonList("inactive"));
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

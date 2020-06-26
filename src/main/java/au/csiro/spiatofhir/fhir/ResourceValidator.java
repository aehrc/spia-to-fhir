/*
 * Copyright 2020 Australian e-Health Research Centre, CSIRO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.csiro.spiatofhir.fhir;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.leftPad;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import org.apache.commons.text.WordUtils;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author John Grimes
 */
public abstract class ResourceValidator {

  private static final Logger logger = LoggerFactory.getLogger(ResourceValidator.class);
  private final FhirValidator validator;

  public ResourceValidator(FhirContext fhirContext, Collection<String> profilePaths)
      throws IOException {
    validator = fhirContext.newValidator();
    ValidationSupportChain validationSupportChain = new ValidationSupportChain();
    PrePopulatedValidationSupport nctsProfilesSupport = new PrePopulatedValidationSupport(
        fhirContext);
    for (String path : profilePaths) {
      IBaseResource profile = getResource(fhirContext, path);
      nctsProfilesSupport.addStructureDefinition(profile);
    }
    validationSupportChain.addValidationSupport(new DefaultProfileValidationSupport(fhirContext));
    validationSupportChain.addValidationSupport(nctsProfilesSupport);
    validationSupportChain
        .addValidationSupport(new InMemoryTerminologyServerValidationSupport(fhirContext));
    CachingValidationSupport cachedValidationSupport = new CachingValidationSupport(
        validationSupportChain);
    FhirInstanceValidator instanceValidator = new FhirInstanceValidator(cachedValidationSupport);
    validator.registerValidatorModule(instanceValidator);
  }

  private static IBaseResource getResource(FhirContext fhirContext, String path)
      throws IOException {
    try (InputStream resourceStream = ResourceValidator.class.getResourceAsStream(path)) {
      return fhirContext.newJsonParser()
          .parseResource(new InputStreamReader(resourceStream));
    }
  }

  public void validate(IBaseResource resource) {
    ValidationResult validationResult = validator.validateWithResult(resource);
    if (validationResult.isSuccessful()) {
      logger.info("Resource is valid, no errors found");
    } else {
      logger.error("Validation errors encountered");
      logResult(validationResult);
    }
  }

  public void logResult(ValidationResult result) {
    StringBuilder output = new StringBuilder("Validation results:");
    int count = 0;
    for (SingleValidationMessage next : result.getMessages()) {
      count++;
      output.append("\n");
      String leftString = "Issue " + count + ": ";
      int leftWidth = leftString.length();
      output.append(leftString);
      if (next.getSeverity() != null) {
        output.append(next.getSeverity()).append(" - ");
      }
      if (isNotBlank(next.getLocationString())) {
        output.append(next.getLocationString());
      }
      String[] message = WordUtils.wrap(next.getMessage(), 80 - leftWidth, "\n", true).split("\\n");
      for (String line : message) {
        output.append("\n");
        output.append(leftPad("", leftWidth)).append(line);
      }
    }
    logger.error(output.toString());
  }

}

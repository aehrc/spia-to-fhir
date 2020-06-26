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

package au.csiro.spiatofhir.fhir.r4;

import au.csiro.spiatofhir.fhir.ResourceValidator;
import ca.uhn.fhir.context.FhirContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author John Grimes
 */
public class R4ResourceValidator extends ResourceValidator {

  private static final List<String> PROFILE_PATHS = Arrays.asList(
      "/fhir/NCTS-Complete-Code-System-4.0.0.json",
      "/fhir/NCTS-Composed-Value-Set-4.0.0.json",
      "/fhir/NCTS-General-Concept-Map-4.0.0.json"
  );

  public R4ResourceValidator(FhirContext fhirContext) throws IOException {
    super(fhirContext, PROFILE_PATHS);
  }

}

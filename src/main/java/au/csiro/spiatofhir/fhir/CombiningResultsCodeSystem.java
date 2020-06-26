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

import au.csiro.spiatofhir.utils.Strings;

/**
 * @author John Grimes
 */
public abstract class CombiningResultsCodeSystem {

  public static final String NAME = "spia-combining-results-flag";
  public static final String ID = NAME + "-" + Strings.majorVersionFromSemVer(FhirResource.VERSION);
  public static final String URL = "https://www.rcpa.edu.au/fhir/CodeSystem/" + ID;
  public static final String VALUESET_URL = "https://www.rcpa.edu.au/fhir/ValueSet/" + ID;
  public static final String OID = "1.2.36.1.2001.1004.300.100.1013";
  public static final String TITLE = "RCPA - SPIA Combining Results Flag";
  public static final String DESCRIPTION = "Codes describing the values for the combining results flag within the SPIA standard.";
  public static final String SHAREABLE_PROFILE_URL = "http://hl7.org/fhir/StructureDefinition/shareablecodesystem";

}

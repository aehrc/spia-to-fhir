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
public abstract class RequestingValueSet {

  public static final String NAME = "spia-requesting-refset";
  public static final String ID = NAME + "-" + Strings.majorVersionFromSemVer(FhirResource.VERSION);
  public static final String URL = "https://www.rcpa.edu.au/fhir/ValueSet/" + ID;
  public static final String OID = "1.2.36.1.2001.1004.300.100.1000";
  public static final String TITLE = "RCPA - SPIA Requesting Pathology Terminology Reference Set";
  public static final String DESCRIPTION =
      "Standard codes for use in requesting pathology tests in Australia, based on the SPIA "
          + "Requesting Pathology Reference Set (v3.1).";
 
}

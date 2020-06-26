/*
 * Copyright 2019 Australian e-Health Research Centre, CSIRO
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

/**
 * @author John Grimes
 */
public abstract class FhirResource {

  public static final String VERSION = "3.0.0";
  public static final String COPYRIGHT =
      "Copyright © The Royal College of Pathologists of Australasia - All rights reserved. "
          + "This content is licensed under a Creative Commons Attribution 4.0 International "
          + "License. See https://creativecommons.org/licenses/by/4.0/.";
  public static final String EMAIL = "help@digitalhealth.gov.au";
  public static final String PUBLISHER = "Australian Digital Health Agency";
  public static final String JURISDICTION_SYSTEM = "urn:iso:std:iso:3166";
  public static final String JURISDICTION_CODE = "AU";
  public static final String JURISDICTION_DISPLAY = "Australia";
  public static final String OID_SYSTEM = "urn:ietf:rfc:3986";

}

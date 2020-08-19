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

    public static final String VERSION = "3.0.1";
    public static final String COPYRIGHT = "Copyright © 2017 - 2020 RCPA. All rights reserved.\n\n" +
            "This material contains content from LOINC (http://loinc.org). LOINC is copyright 1995-2020, " +
            "Regenstrief Institute, Inc. and the Logical Observation Identifiers Names and Codes (LOINC) " +
            "Committee and is available at no cost under the license at http://loinc.org/license. LOINC® is a " +
            "registered United States trademark of Regenstrief Institute, Inc.\n\n" +
            "This material contains information which is protected by copyright. You may download, display, " +
            "print and reproduce any material for your personal, non-commercial use or use within your " +
            "organisation subject to the following terms and conditions:\n\n" +
            "1. The material may not be copied, reproduced, communicated or displayed, in whole or in part, for " +
            "profit or commercial gain.\n" +
            "2. Any copy, reproduction or communication must include this RCPA copyright notice in full.\n" +
            "3. No changes may be made to the wording of the Standards and Guidelines, terminology reference " +
            "sets and information models including commentary, tables or diagrams. Excerpts from the Standards " +
            "and Guidelines, terminology reference sets and information models may be used. References and " +
            "acknowledgments must be maintained in any reproduction or copy in full or part of the " +
            "material.\n\n" +
            "Apart from any use as permitted under the Copyright Act 1968 or as set out above, all other rights " +
            "are reserved. Requests and inquiries concerning reproduction and rights should be addressed to " +
            "RCPA, 207 Albion St, Surry Hills, NSW 2010, Australia.";
    public static final String EMAIL = "help@digitalhealth.gov.au";
    public static final String PUBLISHER = "Australian Digital Health Agency";
    public static final String JURISDICTION_SYSTEM = "urn:iso:std:iso:3166";
    public static final String JURISDICTION_CODE = "AU";
    public static final String JURISDICTION_DISPLAY = "Australia";
    public static final String OID_SYSTEM = "urn:ietf:rfc:3986";

}

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

package au.csiro.spiatofhir.fhir;

import au.csiro.spiatofhir.spia.RefsetEntry;
import org.hl7.fhir.dstu3.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author John Grimes
 */
public interface SpiaFhirValueSet {

    static void addCommonElementsToValueSet(ValueSet valueSet) {
        valueSet.setStatus(Enumerations.PublicationStatus.DRAFT);
        valueSet.setExperimental(true);
        valueSet.setDate(new Date());
        valueSet.setPublisher("Australian E-Health Research Centre, CSIRO");
        List<ContactDetail> contact = new ArrayList<>();
        ContactDetail contactDetail = new ContactDetail();
        ContactPoint contactPoint = new ContactPoint();
        contactPoint.setSystem(ContactPoint.ContactPointSystem.EMAIL);
        contactPoint.setValue("enquiries@aehrc.com");
        contactDetail.addTelecom(contactPoint);
        contact.add(contactDetail);
        valueSet.setContact(contact);
        List<CodeableConcept> jurisdiction = new ArrayList<>();
        CodeableConcept jurisdictionCodeableConcept = new CodeableConcept();
        Coding jurisdictionCoding = new Coding();
        jurisdictionCoding.setSystem("urn:iso:std:iso:3166");
        jurisdictionCoding.setCode("AU");
        jurisdictionCoding.setDisplay("Australia");
        jurisdictionCodeableConcept.addCoding(jurisdictionCoding);
        jurisdiction.add(jurisdictionCodeableConcept);
        valueSet.setJurisdiction(jurisdiction);
        valueSet.setImmutable(true);
    }

    static ValueSet.ValueSetComposeComponent buildComposeFromEntries(List<RefsetEntry> refsetEntries, String system) {
        ValueSet.ValueSetComposeComponent compose = new ValueSet.ValueSetComposeComponent();
        List<ValueSet.ConceptSetComponent> include = new ArrayList<>();
        ValueSet.ConceptSetComponent includeEntry = new ValueSet.ConceptSetComponent();
        List<ValueSet.ConceptReferenceComponent> concept = new ArrayList<>();
        for (RefsetEntry entry : refsetEntries) {
            if (entry.getCode().isPresent()) {
                includeEntry.setSystem(system);
                ValueSet.ConceptReferenceComponent conceptEntry = new ValueSet.ConceptReferenceComponent();
                conceptEntry.setCode(entry.getCode().get());
                if (entry.getRcpaPreferredTerm().isPresent()) {
                    conceptEntry.setDisplay(entry.getRcpaPreferredTerm().get());
                }
                if (!entry.getRcpaSynonyms().isEmpty()) {
                    List<ValueSet.ConceptReferenceDesignationComponent> designation = new ArrayList<>();
                    for (String rcpaSynonym : entry.getRcpaSynonyms()) {
                        ValueSet.ConceptReferenceDesignationComponent designationEntry =
                                new ValueSet.ConceptReferenceDesignationComponent();
                        designationEntry.setValue(rcpaSynonym);
                        Coding designationUse = new Coding();
                        designationUse.setSystem("http://snomed.info/sct");
                        designationUse.setCode("900000000000013009");
                        designationUse.setDisplay("Synonym");
                        designationEntry.setUse(designationUse);
                        designation.add(designationEntry);
                    }
                    conceptEntry.setDesignation(designation);
                }
                concept.add(conceptEntry);
            }
        }
        includeEntry.setConcept(concept);
        include.add(includeEntry);
        compose.setInclude(include);
        return compose;
    }

    ValueSet getValueSet();

}

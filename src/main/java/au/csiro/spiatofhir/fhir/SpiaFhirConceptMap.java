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

import au.csiro.spiatofhir.spia.LoincRefsetEntry;
import au.csiro.spiatofhir.spia.RefsetEntry;
import org.hl7.fhir.dstu3.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author John Grimes
 */
public interface SpiaFhirConceptMap {

    static void addCommonElementsToConceptMap(ConceptMap conceptMap) {
        conceptMap.setStatus(Enumerations.PublicationStatus.DRAFT);
        conceptMap.setExperimental(true);
        conceptMap.setDate(new Date());
        conceptMap.setPublisher("Australian E-Health Research Centre, CSIRO");
        List<ContactDetail> contact = new ArrayList<>();
        ContactDetail contactDetail = new ContactDetail();
        ContactPoint contactPoint = new ContactPoint();
        contactPoint.setSystem(ContactPoint.ContactPointSystem.EMAIL);
        contactPoint.setValue("enquiries@aehrc.com");
        contactDetail.addTelecom(contactPoint);
        contact.add(contactDetail);
        conceptMap.setContact(contact);
        List<CodeableConcept> jurisdiction = new ArrayList<>();
        CodeableConcept jurisdictionCodeableConcept = new CodeableConcept();
        Coding jurisdictionCoding = new Coding();
        jurisdictionCoding.setSystem("urn:iso:std:iso:3166");
        jurisdictionCoding.setCode("AU");
        jurisdictionCoding.setDisplay("Australia");
        jurisdictionCodeableConcept.addCoding(jurisdictionCoding);
        jurisdiction.add(jurisdictionCodeableConcept);
        conceptMap.setJurisdiction(jurisdiction);
    }

    static ConceptMap.ConceptMapGroupComponent buildGroupFromEntries(List<RefsetEntry> refsetEntries) {
        ConceptMap.ConceptMapGroupComponent group = new ConceptMap.ConceptMapGroupComponent();
        for (RefsetEntry entry : refsetEntries) {
            LoincRefsetEntry loincEntry = (LoincRefsetEntry) entry;
            if (loincEntry.getCode() == null || loincEntry.getUcumCode() == null) continue;
            ConceptMap.SourceElementComponent element = new ConceptMap.SourceElementComponent();
            element.setCode(loincEntry.getCode());
            if (loincEntry.getNativeDisplay() != null) element.setDisplay(loincEntry.getNativeDisplay());
            ConceptMap.TargetElementComponent target = new ConceptMap.TargetElementComponent();
            if (loincEntry.getUcumCode().isEmpty()) continue;
            target.setCode(loincEntry.getUcumCode());
            if (loincEntry.getUcumDisplay() != null) target.setDisplay(loincEntry.getUcumDisplay());
            target.setEquivalence(Enumerations.ConceptMapEquivalence.RELATEDTO);
            element.getTarget().add(target);
            group.getElement().add(element);
        }
        return group;
    }

}

/*
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
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
            if (!loincEntry.getCode().isPresent() || !loincEntry.getUcum().isPresent()) continue;
            ConceptMap.SourceElementComponent element = new ConceptMap.SourceElementComponent();
            element.setCode(loincEntry.getCode().get());
            if (loincEntry.getRcpaPreferredTerm().isPresent())
                element.setDisplay(loincEntry.getRcpaPreferredTerm().get());
            ConceptMap.TargetElementComponent target = new ConceptMap.TargetElementComponent();
            target.setCode(loincEntry.getUcum().get());
            if (loincEntry.getUnit().isPresent())
                target.setDisplay(loincEntry.getUnit().get());
            target.setEquivalence(Enumerations.ConceptMapEquivalence.RELATEDTO);
            element.getTarget().add(target);
            group.getElement().add(element);
        }
        return group;
    }

}

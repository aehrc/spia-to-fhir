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

import au.csiro.spiatofhir.spia.ChemicalPathologyRefsetEntry;
import au.csiro.spiatofhir.spia.LoincRefsetEntry;
import au.csiro.spiatofhir.spia.RefsetEntry;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

/**
 * @author John Grimes
 */
public interface SpiaFhirConceptMap {

  /**
   * Populates the elements that are common to all ConceptMaps.
   */
  static void addCommonElementsToConceptMap(ConceptMap conceptMap) {
    Meta meta = new Meta();
    List<UriType> profile = new ArrayList<>();
    profile.add(new UriType(
        "https://healthterminologies.gov.au/fhir/StructureDefinition/general-concept-map-2"));
    meta.setProfile(profile);
    conceptMap.setMeta(meta);
    Narrative text = new Narrative();
    text.setStatus(Narrative.NarrativeStatus.GENERATED);
    XhtmlNode div = new XhtmlNode(NodeType.Element, "div");
    div.addText(conceptMap.getTitle());
    text.setDiv(div);
    conceptMap.setText(text);
    conceptMap.setStatus(Enumerations.PublicationStatus.DRAFT);
    conceptMap.setExperimental(true);
    conceptMap.setPublisher("Australian Digital Health Agency");
    conceptMap.setCopyright(
        "Copyright © The Royal College of Pathologists of Australasia - All rights reserved. "
            + "This content is licensed under a Creative Commons Attribution 4.0 International "
            + "License. See https://creativecommons.org/licenses/by/4.0/.");
    List<ContactDetail> contact = new ArrayList<>();
    ContactDetail contactDetail = new ContactDetail();
    ContactPoint contactPoint = new ContactPoint();
    contactPoint.setSystem(ContactPoint.ContactPointSystem.EMAIL);
    contactPoint.setValue("help@digitalhealth.gov.au");
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

  /**
   * Builds the group element of a ConceptMap, using a list of reference set entries and their
   * preferred units.
   */
  static ConceptMap.ConceptMapGroupComponent buildPreferredUnitGroupFromEntries(
      List<RefsetEntry> refsetEntries) {
    ConceptMap.ConceptMapGroupComponent group = new ConceptMap.ConceptMapGroupComponent();
    for (RefsetEntry entry : refsetEntries) {
      LoincRefsetEntry loincEntry = (LoincRefsetEntry) entry;
      if (loincEntry.getCode() == null || loincEntry.getUcumCode() == null) {
        continue;
      }
      ConceptMap.SourceElementComponent element = new ConceptMap.SourceElementComponent();
      element.setCode(loincEntry.getCode());
      ConceptMap.TargetElementComponent target = new ConceptMap.TargetElementComponent();
      if (loincEntry.getUcumCode().isEmpty()) {
        continue;
      }
      target.setCode(loincEntry.getUcumCode());
      target.setEquivalence(Enumerations.ConceptMapEquivalence.RELATEDTO);
      element.getTarget().add(target);
      group.getElement().add(element);
    }
    return group;
  }

  /**
   * Builds the group element of a ConceptMap, using a list of reference set entries and their
   * combining results flags.
   */
  static ConceptMap.ConceptMapGroupComponent buildCombiningResultsFlagsGroupFromEntries(
      List<RefsetEntry> refsetEntries) {
    ConceptMap.ConceptMapGroupComponent group = new ConceptMap.ConceptMapGroupComponent();
    for (RefsetEntry entry : refsetEntries) {
      ChemicalPathologyRefsetEntry chemicalEntry = (ChemicalPathologyRefsetEntry) entry;
      if (chemicalEntry.getCode() == null || chemicalEntry.getCombiningResultsFlag() == null) {
        continue;
      }
      ConceptMap.SourceElementComponent element = new ConceptMap.SourceElementComponent();
      element.setCode(chemicalEntry.getCode());
      ConceptMap.TargetElementComponent target = new ConceptMap.TargetElementComponent();
      target.setCode(chemicalEntry.getCombiningResultsFlag().getCode());
      target.setEquivalence(Enumerations.ConceptMapEquivalence.RELATEDTO);
      element.getTarget().add(target);
      group.getElement().add(element);
    }
    return group;
  }

}

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

import static org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem.EMAIL;
import static org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.DRAFT;
import static org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus.GENERATED;

import au.csiro.spiatofhir.snomed.SnomedCt;
import au.csiro.spiatofhir.spia.RefsetEntry;
import au.csiro.spiatofhir.utils.Markdown;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

/**
 * Common functionality relating to the creation of FHIR ValueSets from the SPIA reference sets.
 *
 * @author John Grimes
 */
public abstract class SpiaFhirValueSet extends SpiaFhirResource {

  /**
   * Populates the elements that are common to all ValueSets.
   */
  static void addCommonElementsToValueSet(ValueSet valueSet) {
    Meta meta = new Meta();
    List<UriType> profile = new ArrayList<>();
    profile.add(new UriType("http://hl7.org/fhir/StructureDefinition/shareablevalueset"));
    profile.add(new UriType(
        "https://healthterminologies.gov.au/fhir/StructureDefinition/composed-value-set-2"));
    meta.setProfile(profile);
    valueSet.setMeta(meta);
    Narrative text = new Narrative();
    text.setStatus(GENERATED);
    XhtmlNode div = new XhtmlNode(NodeType.Element, "div");
    div.setValueAsString(
        "<div><h1>" + valueSet.getTitle() + "</h1>" + Markdown.toHtml(valueSet.getDescription())
            + "</div>");
    text.setDiv(div);
    valueSet.setText(text);
    valueSet.setStatus(DRAFT);
    valueSet.setExperimental(true);
    valueSet.setPublisher("Australian Digital Health Agency");
    valueSet.setCopyright(
        "Copyright © The Royal College of Pathologists of Australasia - All rights reserved. "
            + "This content is licensed under a Creative Commons Attribution 4.0 International "
            + "License. See https://creativecommons.org/licenses/by/4.0/.");
    List<ContactDetail> contact = new ArrayList<>();
    ContactDetail contactDetail = new ContactDetail();
    ContactPoint contactPoint = new ContactPoint();
    contactPoint.setSystem(EMAIL);
    contactPoint.setValue("help@digitalhealth.gov.au");
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
  }

  /**
   * Builds the compose element of a ValueSet, using a list of reference set entries.
   */
  static ValueSet.ValueSetComposeComponent buildComposeFromEntries(List<RefsetEntry> refsetEntries,
      String system) {
    ValueSet.ValueSetComposeComponent compose = new ValueSet.ValueSetComposeComponent();
    List<ValueSet.ConceptSetComponent> include = new ArrayList<>();
    ValueSet.ConceptSetComponent includeEntry = new ValueSet.ConceptSetComponent();
    List<ValueSet.ConceptReferenceComponent> concept = new ArrayList<>();

    for (RefsetEntry entry : refsetEntries) {
      if (entry.getCode() != null) {
        includeEntry.setSystem(system);
        ValueSet.ConceptReferenceComponent conceptEntry = new ValueSet.ConceptReferenceComponent();
        conceptEntry.setCode(entry.getCode());
        // RCPA preferred term is used as the display term within the ValueSet definition.
        // See: https://www.hl7.org/fhir/STU3/valueset-definitions.html#ValueSet.compose.include.concept.display
        conceptEntry.setDisplay(entry.getRcpaPreferredTerm());

        // RCPA synonyms are added as designations, coded with the SNOMED code 900000000000013009|Synonym.
        // See: https://www.hl7.org/fhir/STU3/valueset-definitions.html#ValueSet.compose.include.concept.designation
        if (!entry.getRcpaSynonyms().isEmpty()) {
          List<ValueSet.ConceptReferenceDesignationComponent> designation = new ArrayList<>(
              buildSynonymDesignations(entry));
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

  /**
   * Builds a designation element for a synonym.
   */
  static List<ValueSet.ConceptReferenceDesignationComponent> buildSynonymDesignations(
      RefsetEntry entry) {
    List<ValueSet.ConceptReferenceDesignationComponent> designationEntries = new ArrayList<>();
    for (String rcpaSynonym : entry.getRcpaSynonyms()) {
      ValueSet.ConceptReferenceDesignationComponent designationEntry =
          new ValueSet.ConceptReferenceDesignationComponent();
      designationEntry.setValue(rcpaSynonym);
      Coding designationUse = new Coding(
          SnomedCt.SYSTEM_URI,
          "900000000000013009",
          "Synonym");
      designationEntry.setUse(designationUse);
      designationEntries.add(designationEntry);
    }
    return designationEntries;
  }

}

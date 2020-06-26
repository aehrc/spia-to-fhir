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

package au.csiro.spiatofhir.fhir.stu3;

import au.csiro.spiatofhir.fhir.FhirResource;
import au.csiro.spiatofhir.spia.RefsetEntry;
import au.csiro.spiatofhir.utils.Markdown;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.dstu3.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.dstu3.model.ContactDetail;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

/**
 * Common functionality relating to the creation of FHIR ConceptMaps from the SPIA reference sets.
 *
 * @author John Grimes
 */
public abstract class Stu3ConceptMap {

  public static final String NCTS_PROFILE_URL = "https://healthterminologies.gov.au/fhir/StructureDefinition/general-concept-map-2";

  /**
   * Populates the elements that are common to all ConceptMaps.
   */
  static void addCommonElementsToConceptMap(ConceptMap conceptMap) {
    Meta meta = new Meta();
    List<UriType> profile = new ArrayList<>();
    profile.add(new UriType(NCTS_PROFILE_URL));
    meta.setProfile(profile);
    conceptMap.setMeta(meta);
    Narrative text = new Narrative();
    text.setStatus(NarrativeStatus.GENERATED);
    XhtmlNode div = new XhtmlNode(NodeType.Element, "div");
    div.setValueAsString(
        "<div><h1>" + conceptMap.getTitle() + "</h1>" + Markdown.toHtml(conceptMap.getDescription())
            + "</div>");
    text.setDiv(div);
    conceptMap.setText(text);
    conceptMap.setStatus(PublicationStatus.DRAFT);
    conceptMap.setExperimental(true);
    conceptMap.setPublisher(FhirResource.PUBLISHER);
    conceptMap.setCopyright(FhirResource.COPYRIGHT);
    List<ContactDetail> contact = new ArrayList<>();
    ContactDetail contactDetail = new ContactDetail();
    ContactPoint contactPoint = new ContactPoint();
    contactPoint.setSystem(ContactPointSystem.EMAIL);
    contactPoint.setValue(FhirResource.EMAIL);
    contactDetail.addTelecom(contactPoint);
    contact.add(contactDetail);
    conceptMap.setContact(contact);
    List<CodeableConcept> jurisdiction = new ArrayList<>();
    CodeableConcept jurisdictionCodeableConcept = new CodeableConcept();
    Coding jurisdictionCoding = new Coding();
    jurisdictionCoding.setSystem(FhirResource.JURISDICTION_SYSTEM);
    jurisdictionCoding.setCode(FhirResource.JURISDICTION_CODE);
    jurisdictionCoding.setDisplay(FhirResource.JURISDICTION_DISPLAY);
    jurisdictionCodeableConcept.addCoding(jurisdictionCoding);
    jurisdiction.add(jurisdictionCodeableConcept);
    conceptMap.setJurisdiction(jurisdiction);
  }

  /**
   * Builds the group element of a ConceptMap, using a list of reference set entries and their
   * preferred units.
   */
  static ConceptMapGroupComponent buildPreferredUnitGroupFromEntries(
      List<RefsetEntry> refsetEntries) {
    ConceptMapGroupComponent group = new ConceptMapGroupComponent();
    for (RefsetEntry entry : refsetEntries) {
      assert entry.getCode() != null;
      if (entry.getUnitCodes().isEmpty()) {
        continue;
      }
      SourceElementComponent element = new SourceElementComponent();
      element.setCode(entry.getCode());
      for (String unitCode : entry.getUnitCodes()) {
        TargetElementComponent target = new TargetElementComponent();
        target.setCode(unitCode);
        target.setEquivalence(Enumerations.ConceptMapEquivalence.RELATEDTO);
        element.getTarget().add(target);
      }
      group.getElement().add(element);
    }
    return group;
  }

  /**
   * Builds the group element of a ConceptMap, using a list of reference set entries and their
   * combining results flags.
   */
  static ConceptMapGroupComponent buildCombiningResultsFlagsGroupFromEntries(
      List<RefsetEntry> refsetEntries) {
    ConceptMapGroupComponent group = new ConceptMapGroupComponent();
    for (RefsetEntry entry : refsetEntries) {
      assert entry.getCode() != null;
      if (entry.getCombiningResultsFlag() == null) {
        continue;
      }
      SourceElementComponent element = new SourceElementComponent();
      element.setCode(entry.getCode());
      TargetElementComponent target = new TargetElementComponent();
      target.setCode(entry.getCombiningResultsFlag().getCode());
      target.setEquivalence(ConceptMapEquivalence.RELATEDTO);
      element.getTarget().add(target);
      group.getElement().add(element);
    }
    return group;
  }

}

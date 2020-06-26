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
import au.csiro.spiatofhir.fhir.SpiaValueSet;
import au.csiro.spiatofhir.snomed.SnomedCt;
import au.csiro.spiatofhir.spia.RefsetEntry;
import au.csiro.spiatofhir.utils.Markdown;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactDetail;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

/**
 * Common functionality relating to the creation of FHIR ValueSets from the SPIA reference sets.
 *
 * @author John Grimes
 */
public abstract class Stu3ValueSet {

  public static final String NCTS_PROFILE_URL = "https://healthterminologies.gov.au/fhir/StructureDefinition/composed-value-set-2";

  /**
   * Populates the elements that are common to all ValueSets.
   */
  static void addCommonElementsToValueSet(ValueSet valueSet) {
    Meta meta = new Meta();
    List<UriType> profile = new ArrayList<>();
    profile.add(new UriType(SpiaValueSet.SHAREABLE_PROFILE_URL));
    profile.add(new UriType(NCTS_PROFILE_URL));
    meta.setProfile(profile);
    valueSet.setMeta(meta);
    Narrative text = new Narrative();
    text.setStatus(NarrativeStatus.GENERATED);
    XhtmlNode div = new XhtmlNode(NodeType.Element, "div");
    div.setValueAsString(
        "<div><h1>" + valueSet.getTitle() + "</h1>" + Markdown.toHtml(valueSet.getDescription())
            + "</div>");
    text.setDiv(div);
    valueSet.setText(text);
    valueSet.setStatus(PublicationStatus.DRAFT);
    valueSet.setExperimental(true);
    valueSet.setPublisher(FhirResource.PUBLISHER);
    valueSet.setCopyright(
        FhirResource.COPYRIGHT);
    List<ContactDetail> contact = new ArrayList<>();
    ContactDetail contactDetail = new ContactDetail();
    ContactPoint contactPoint = new ContactPoint();
    contactPoint.setSystem(ContactPointSystem.EMAIL);
    contactPoint.setValue(FhirResource.EMAIL);
    contactDetail.addTelecom(contactPoint);
    contact.add(contactDetail);
    valueSet.setContact(contact);
    List<CodeableConcept> jurisdiction = new ArrayList<>();
    CodeableConcept jurisdictionCodeableConcept = new CodeableConcept();
    Coding jurisdictionCoding = new Coding();
    jurisdictionCoding.setSystem(FhirResource.JURISDICTION_SYSTEM);
    jurisdictionCoding.setCode(FhirResource.JURISDICTION_CODE);
    jurisdictionCoding.setDisplay(FhirResource.JURISDICTION_DISPLAY);
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
      Coding designationUse = new Coding(SnomedCt.SYSTEM_URI, SpiaValueSet.SYNONYM_CODE,
          SpiaValueSet.SYNONYM_DISPLAY);
      designationEntry.setUse(designationUse);
      designationEntries.add(designationEntry);
    }
    return designationEntries;
  }

}

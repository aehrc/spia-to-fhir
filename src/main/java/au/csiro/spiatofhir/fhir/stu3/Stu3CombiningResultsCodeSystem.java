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

import au.csiro.spiatofhir.fhir.CombiningResultsCodeSystem;
import au.csiro.spiatofhir.fhir.FhirResource;
import au.csiro.spiatofhir.utils.Markdown;
import au.csiro.spiatofhir.utils.Strings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactDetail;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

/**
 * @author John Grimes
 */
public class Stu3CombiningResultsCodeSystem {

  public static final String NCTS_PROFILE_URL = "https://healthterminologies.gov.au/fhir/StructureDefinition/complete-code-system-2";

  private final Date publicationDate;

  public Stu3CombiningResultsCodeSystem(Date publicationDate) {
    this.publicationDate = publicationDate;
  }

  public CodeSystem build() {
    CodeSystem codeSystem = new CodeSystem();

    codeSystem.setVersion(FhirResource.VERSION);
    codeSystem.setId(CombiningResultsCodeSystem.NAME + "-" + Strings
        .majorVersionFromSemVer(FhirResource.VERSION));
    codeSystem.setUrl(CombiningResultsCodeSystem.URL);
    codeSystem.setValueSet(CombiningResultsCodeSystem.VALUESET_URL);
    Identifier oid = new Identifier();
    oid.setSystem(FhirResource.OID_SYSTEM);
    oid.setValue("urn:oid:" + CombiningResultsCodeSystem.OID);
    codeSystem.setIdentifier(oid);
    codeSystem.setTitle(CombiningResultsCodeSystem.TITLE);
    codeSystem.setName(CombiningResultsCodeSystem.NAME);
    codeSystem.setDescription(CombiningResultsCodeSystem.DESCRIPTION);
    codeSystem.setDate(publicationDate);
    codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
    codeSystem.setExperimental(false);
    codeSystem.setContent(CodeSystemContentMode.COMPLETE);
    codeSystem.setVersionNeeded(false);
    codeSystem.setCompositional(false);
    codeSystem.setCaseSensitive(false);

    Meta meta = new Meta();
    List<UriType> profile = new ArrayList<>();
    profile.add(new UriType(CombiningResultsCodeSystem.SHAREABLE_PROFILE_URL));
    profile.add(new UriType(NCTS_PROFILE_URL));
    meta.setProfile(profile);
    codeSystem.setMeta(meta);
    Narrative text = new Narrative();
    text.setStatus(NarrativeStatus.GENERATED);
    XhtmlNode div = new XhtmlNode(NodeType.Element, "div");
    div.setValueAsString(
        "<div><h1>" + codeSystem.getTitle() + "</h1>" + Markdown.toHtml(codeSystem.getDescription())
            + "</div>");
    text.setDiv(div);
    codeSystem.setText(text);
    codeSystem.setStatus(PublicationStatus.DRAFT);
    codeSystem.setExperimental(true);
    codeSystem.setPublisher(FhirResource.PUBLISHER);
    codeSystem.setCopyright(
        FhirResource.COPYRIGHT);
    List<ContactDetail> contact = new ArrayList<>();
    ContactDetail contactDetail = new ContactDetail();
    ContactPoint contactPoint = new ContactPoint();
    contactPoint.setSystem(ContactPointSystem.EMAIL);
    contactPoint.setValue(FhirResource.EMAIL);
    contactDetail.addTelecom(contactPoint);
    contact.add(contactDetail);
    codeSystem.setContact(contact);
    List<CodeableConcept> jurisdiction = new ArrayList<>();
    CodeableConcept jurisdictionCodeableConcept = new CodeableConcept();
    Coding jurisdictionCoding = new Coding();
    jurisdictionCoding.setSystem(FhirResource.JURISDICTION_SYSTEM);
    jurisdictionCoding.setCode(FhirResource.JURISDICTION_CODE);
    jurisdictionCoding.setDisplay(FhirResource.JURISDICTION_DISPLAY);
    jurisdictionCodeableConcept.addCoding(jurisdictionCoding);
    jurisdiction.add(jurisdictionCodeableConcept);
    codeSystem.setJurisdiction(jurisdiction);

    List<ConceptDefinitionComponent> concepts = new ArrayList<>();

    ConceptDefinitionComponent red = new ConceptDefinitionComponent();
    red.setCode("red");
    red.setDisplay("Red");
    red.setDefinition("This test is known to be unsafe to make comparisons");
    concepts.add(red);

    ConceptDefinitionComponent orange = new ConceptDefinitionComponent();
    orange.setCode("orange");
    orange.setDisplay("Orange");
    orange.setDefinition(
        "This test has either not yet been considered or there is uncertainty around comparisons");
    concepts.add(orange);

    ConceptDefinitionComponent green = new ConceptDefinitionComponent();
    green.setCode("green");
    green.setDisplay("Green");
    green.setDefinition("This test is considered safe to combine if harmonised");
    concepts.add(green);

    codeSystem.setConcept(concepts);
    codeSystem.setCount(3);

    return codeSystem;
  }

}

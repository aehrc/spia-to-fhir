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

package au.csiro.spiatofhir;

import au.csiro.spiatofhir.fhir.TerminologyClient;
import au.csiro.spiatofhir.fhir.r4.R4Bundle;
import au.csiro.spiatofhir.fhir.stu3.Stu3Bundle;
import au.csiro.spiatofhir.spia.SpiaDistribution;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Takes the SPIA distribution located at the `inputPath`, transforms it into a set of FHIR
 * resources, then outputs a FHIR JSON Bundle to the `outputDirectory`.
 * <p>
 * Requires the help of a FHIR terminology server, accessible at `terminologyServerUrl`, for
 * populating native display terms within ValueSets and ConceptMaps.
 *
 * @author John Grimes
 */
@Mojo(name = "transform")
public class SpiaToFhirMavenPlugin extends AbstractMojo {

  private static final Logger logger = LoggerFactory.getLogger(SpiaToFhirMavenPlugin.class);
  private static final String PUBLICATION_DATE_PATTERN = "yyyy-MM-dd";

  @Parameter(property = "inputPath", required = true)
  private String inputPath;

  @Parameter(property = "outputDirectory", required = true)
  private String outputDirectory;

  @Parameter(property = "terminologyServerUrl", required = true)
  private String terminologyServerUrl;

  @Parameter(property = "publicationDate", required = true)
  private String publicationDate;

  @Override
  public void execute() throws MojoExecutionException {
    try {
      FhirContext r4Context = FhirContext.forR4();
      TerminologyClient terminologyClient = r4Context
          .newRestfulClient(TerminologyClient.class, terminologyServerUrl);
      UcumService ucumService = new UcumEssenceService(Thread.currentThread()
          .getContextClassLoader()
          .getResourceAsStream("ucum-essence.xml"));
      File inputFile = new File(inputPath);
      SimpleDateFormat publicationDateFormat = new SimpleDateFormat(PUBLICATION_DATE_PATTERN);

      // Parse RCPA distribution.
      SpiaDistribution spiaDistribution = new SpiaDistribution(inputFile, terminologyClient,
          ucumService);

      // Convert distribution into a STU3 Bundle.
      Stu3Bundle stu3BundleBuilder = new Stu3Bundle(
          spiaDistribution,
          publicationDateFormat.parse(publicationDate)
      );
      org.hl7.fhir.dstu3.model.Bundle stu3Bundle = stu3BundleBuilder.getBundle();
      FhirContext stu3Context = FhirContext.forDstu3();
      IParser stu3Parser = stu3Context.newJsonParser();
      String stu3Json = stu3Parser.encodeResourceToString(stu3Bundle);
      String stu3OutputPath = Paths.get(outputDirectory, "/spia-stu3.Bundle.json").toAbsolutePath()
          .toString();
      try (FileWriter fileWriter = new FileWriter(stu3OutputPath)) {
        fileWriter.write(stu3Json);
      }

      // Convert distribution into a R4 Bundle.
      R4Bundle r4BundleBuilder = new R4Bundle(
          spiaDistribution,
          publicationDateFormat.parse(publicationDate)
      );
      org.hl7.fhir.r4.model.Bundle r4Bundle = r4BundleBuilder.getBundle();
      IParser r4JsonParser = r4Context.newJsonParser();
      String r4Json = r4JsonParser.encodeResourceToString(r4Bundle);
      String r4OutputPath = Paths.get(outputDirectory, "/spia-r4.Bundle.json").toAbsolutePath()
          .toString();
      try (FileWriter fileWriter = new FileWriter(r4OutputPath)) {
        fileWriter.write(r4Json);
      }
    } catch (Exception e) {
      logger.error("Error occurred during execution: ", e);
      throw new MojoExecutionException("Error occurred during execution: ", e);
    }
  }

}

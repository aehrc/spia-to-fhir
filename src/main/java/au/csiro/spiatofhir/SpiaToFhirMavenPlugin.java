/*
 *    Copyright 2019 Australian e-Health Research Centre, CSIRO
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

package au.csiro.spiatofhir;

import au.csiro.spiatofhir.fhir.SpiaFhirBundle;
import au.csiro.spiatofhir.fhir.TerminologyClient;
import au.csiro.spiatofhir.spia.SpiaDistribution;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumService;
import org.hl7.fhir.dstu3.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;

/**
 * Takes the SPIA distribution located at the `inputPath`, transforms it into a set of FHIR resources, then outputs a
 * FHIR JSON Bundle to the `outputPath`.
 * <p>
 * Requires the help of a FHIR terminology server, accessible at `terminologyServerUrl`, for populating native
 * display terms within ValueSets and ConceptMaps.
 *
 * @author John Grimes
 */
@Mojo(name = "transform")
public class SpiaToFhirMavenPlugin extends AbstractMojo {

    private static final Logger logger = LoggerFactory.getLogger(SpiaToFhirMavenPlugin.class);

    @Parameter(property = "inputPath", required = true)
    private String inputPath;

    @Parameter(property = "outputPath", required = true)
    private String outputPath;

    @Parameter(property = "terminologyServerUrl", required = true)
    private String terminologyServerUrl;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            FhirContext fhirContext = FhirContext.forDstu3();
            TerminologyClient terminologyClient = new TerminologyClient(fhirContext, terminologyServerUrl);
            UcumService ucumService = new UcumEssenceService(Thread.currentThread()
                                                                   .getContextClassLoader()
                                                                   .getResourceAsStream("ucum-essence.xml"));
            File inputFile = new File(inputPath);

            // Parse RCPA distribution.
            SpiaDistribution spiaDistribution = new SpiaDistribution(inputFile, terminologyClient, ucumService);

            // Convert distribution into a FHIR Bundle.
            SpiaFhirBundle spiaFhirBundle = new SpiaFhirBundle(fhirContext, spiaDistribution);
            Bundle transformed = spiaFhirBundle.getBundle();

            // Encode the Bundle to JSON and write to the output path.
            IParser jsonParser = fhirContext.newJsonParser();
            String json = jsonParser.encodeResourceToString(transformed);
            try (FileWriter fileWriter = new FileWriter(outputPath)) {
                fileWriter.write(json);
            }
        } catch (Exception e) {
            logger.error("Error occurred during execution: ", e);
            throw new MojoExecutionException("Error occurred during execution: ", e);
        }
    }

}

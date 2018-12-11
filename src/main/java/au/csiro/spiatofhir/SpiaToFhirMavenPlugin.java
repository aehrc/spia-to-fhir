/*
 * Copyright © Australian e-Health Research Centre, CSIRO. All rights reserved.
 */

package au.csiro.spiatofhir;

import au.csiro.spiatofhir.fhir.SpiaFhirBundle;
import au.csiro.spiatofhir.spia.SpiaDistribution;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.hl7.fhir.dstu3.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;

/**
 * @author John Grimes
 */
@Mojo(name = "transform")
public class SpiaToFhirMavenPlugin extends AbstractMojo {

    private static Logger logger = LoggerFactory.getLogger(SpiaToFhirMavenPlugin.class);

    @Parameter(property = "SpiaToFhir.inputPath", required = true)
    private String inputPath;

    @Parameter(property = "SpiaToFhir.outputPath", required = true)
    private String outputPath;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            File inputFile = new File(inputPath);

            // Parse RCPA distribution.
            SpiaDistribution spiaDistribution = new SpiaDistribution(inputFile);

            // Convert distribution into a FHIR Bundle.
            SpiaFhirBundle spiaFhirBundle = new SpiaFhirBundle(spiaDistribution);
            Bundle transformed = spiaFhirBundle.getBundle();

            // Encode the Bundle to JSON and write to the output path.
            IParser jsonParser = FhirContext.forDstu3().newJsonParser();
            String json = jsonParser.encodeResourceToString(transformed);
            FileWriter fileWriter = new FileWriter(outputPath);
            fileWriter.write(json);
        } catch (Exception e) {
            logger.error("Error occurred during execution: ", e);
            throw new MojoExecutionException("Error occurred during execution: ", e);
        }
    }

}

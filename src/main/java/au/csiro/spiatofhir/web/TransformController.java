/*
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.web;

import au.csiro.spiatofhir.fhir.SpiaFhirBundle;
import au.csiro.spiatofhir.spia.SpiaDistribution;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author John Grimes
 */
@RestController
public class TransformController {

    static final Logger logger = LoggerFactory.getLogger(TransformController.class);

    @Autowired
    FhirContext fhirContext;

    @RequestMapping(value = "/transform", method = RequestMethod.POST)
    public ResponseEntity<String> transform(@RequestParam("file") MultipartFile file) {
        try {
            // Copy file into a temporary area.
            File tempFile = File.createTempFile("spiatofhir", ".zip");
            file.transferTo(tempFile);

            // Parse RCPA distribution.
            SpiaDistribution spiaDistribution = new SpiaDistribution(tempFile);

            // Convert distribution into a FHIR Bundle.
            SpiaFhirBundle spiaFhirBundle = new SpiaFhirBundle(spiaDistribution);
            Bundle transformed = spiaFhirBundle.getBundle();

            // Encode the Bundle to JSON and return in the response
            String encoded = fhirContext.newJsonParser().encodeResourceToString(transformed);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json+fhir");
            return new ResponseEntity<String>(encoded, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An exception occurred during the transformation: ", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

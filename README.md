# SPIA to FHIR

A [Maven](https://maven.apache.org/) plugin for converting terminology from version 3 of the [Standards for Pathology Informatics in Australia (SPIA)](https://test.rcpa.edu.au/Library/Practising-Pathology/PTIS/APUTS-Downloads) into a set of [FHIR STU3](https://hl7.org/fhir/STU3/) terminology resources.

The input is the [ZIP archive](https://www.healthterminologies.gov.au/access?content=rcpadownload) distributed on the National Clinical Terminology Service web site.

The output is a FHIR [Bundle](https://hl7.org/fhir/STU3/bundle.html) containing the following resources:

1. Requesting reference set (SNOMED CT ValueSet)
2. Chemical pathology reporting reference set (LOINC ValueSet)
3. Chemical pathology unit map (ConceptMap)
4. Microbiology serology molecular reporting reference set (LOINC ValueSet)
5. Microbiology serology molecular unit map (ConceptMap)
6. Microbiology subset of organisms (SNOMED CT ValueSet)
7. Haematology reporting reference set (LOINC ValueSet)
8. Haematology unit map (ConceptMap)
9. Immunopathology reporting reference set (LOINC ValueSet)
10. Immunopathology unit map (ConceptMap)
11. Preferred unit reference set (UCUM ValueSet)

A FHIR terminology server that contains SNOMED CT, LOINC and UCUM is required for the build process, as it is used to populate the native display terms into the ValueSets and ConceptMaps.

You can execute the plugin with the following commands:

```
mvn install

mvn -DinputPath=[zip file] -DoutputPath=[bundle JSON file] -DterminologyServerUrl=[FHIR terminology server endpoint] au.csiro:spia-to-fhir-maven-plugin:transform
```

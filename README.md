# SPIA to FHIR

A [Maven](https://maven.apache.org/) plugin for converting terminology from the
[Standards for Pathology Informatics in Australia (SPIA)](https://test.rcpa.edu.au/Library/Practising-Pathology/PTIS/APUTS-Downloads)
into a set of [FHIR STU3](https://hl7.org/fhir/STU3/) terminology resources.

The input is the
[ZIP archive](https://www.healthterminologies.gov.au/access?content=rcpadownload)
distributed on the National Clinical Terminology Service web site.

The output is a FHIR [Bundle](https://hl7.org/fhir/STU3/bundle.html) containing
the following ten resources:

1. Requesting reference set (SNOMED CT ValueSet)
2. Chemical pathology reporting reference set (LOINC ValueSet)
3. Chemical pathology unit map (ConceptMap)
4. Microbiology serology molecular reporting reference set (LOINC ValueSet)
5. Microbiology serology molecular unit map (ConceptMap)
6. Haematology reporting reference set (LOINC ValueSet)
7. Haematology unit map (ConceptMap)
8. Immunopathology reporting reference set (LOINC ValueSet)
9. Immunopathology unit map (ConceptMap)
10. Preferred unit reference set (UCUM ValueSet)

You can execute the plugin with the following command:

```
mvn -DSpiaToFhir.inputPath=[zip file] -DSpiaToFhir.outputPath=[bundle JSON file] install
```

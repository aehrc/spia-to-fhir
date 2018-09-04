/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.spia;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UcumRefsetEntry implements RefsetEntry {

    private Optional<String> rcpaPreferredTerm;
    private Optional<String> description;
    private Optional<String> ucumCode;

    public UcumRefsetEntry() {
    }

    @Override
    public Optional<String> getRcpaPreferredTerm() {
        return rcpaPreferredTerm;
    }

    public void setRcpaPreferredTerm(Optional<String> rcpaPreferredTerm) {
        this.rcpaPreferredTerm = rcpaPreferredTerm;
    }

    @Override
    public Set<String> getRcpaSynonyms() {
        return new HashSet<>();
    }

    public Optional<String> getDescription() {
        return description;
    }

    public void setDescription(Optional<String> description) {
        this.description = description;
    }

    public Optional<String> getCode() {
        return ucumCode;
    }

    public void setCode(Optional<String> code) {
        this.ucumCode = code;
    }

}

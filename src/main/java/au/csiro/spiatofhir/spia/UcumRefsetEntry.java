/*
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.spia;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author John Grimes
 */
public class UcumRefsetEntry implements RefsetEntry {

    private String rcpaPreferredTerm;
    private String description;
    private String ucumCode;

    public UcumRefsetEntry() {
    }

    @Override
    public Optional<String> getRcpaPreferredTerm() {
        return Optional.ofNullable(rcpaPreferredTerm);
    }

    public void setRcpaPreferredTerm(Optional<String> rcpaPreferredTerm) {
        this.rcpaPreferredTerm = rcpaPreferredTerm.orElse(null);
    }

    @Override
    public Set<String> getRcpaSynonyms() {
        return new HashSet<>();
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(Optional<String> description) {
        this.description = description.orElse(null);
    }

    public Optional<String> getCode() {
        return Optional.ofNullable(ucumCode);
    }

    public void setCode(Optional<String> code) {
        this.ucumCode = code.orElse(null);
    }

}

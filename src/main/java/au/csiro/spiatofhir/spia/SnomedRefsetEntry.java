/*
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.spia;

import java.util.Optional;
import java.util.Set;

/**
 * @author John Grimes
 */
public class SnomedRefsetEntry implements RefsetEntry {

    private Optional<String> rcpaPreferredTerm;
    private Set<String> rcpaSynonyms;
    private Optional<String> usageGuidance;
    private Optional<Double> length;
    private Optional<String> specimen;
    private Optional<String> snomedCode;
    private Optional<Double> version;
    private Optional<String> history;

    public SnomedRefsetEntry() {
    }

    public Optional<String> getRcpaPreferredTerm() {
        return rcpaPreferredTerm;
    }

    public void setRcpaPreferredTerm(Optional<String> rcpaPreferredTerm) {
        this.rcpaPreferredTerm = rcpaPreferredTerm;
    }

    public Set<String> getRcpaSynonyms() {
        return rcpaSynonyms;
    }

    public void setRcpaSynonyms(Set<String> rcpaSynonyms) {
        this.rcpaSynonyms = rcpaSynonyms;
    }

    public Optional<String> getUsageGuidance() {
        return usageGuidance;
    }

    public void setUsageGuidance(Optional<String> usageGuidance) {
        this.usageGuidance = usageGuidance;
    }

    public Optional<Double> getLength() {
        return length;
    }

    public void setLength(Optional<Double> length) {
        this.length = length;
    }

    public Optional<String> getSpecimen() {
        return specimen;
    }

    public void setSpecimen(Optional<String> specimen) {
        this.specimen = specimen;
    }

    public Optional<String> getCode() {
        return snomedCode;
    }

    public void setCode(Optional<String> code) {
        this.snomedCode = code;
    }

    public Optional<Double> getVersion() {
        return version;
    }

    public void setVersion(Optional<Double> version) {
        this.version = version;
    }

    public Optional<String> getHistory() {
        return history;
    }

    public void setHistory(Optional<String> history) {
        this.history = history;
    }

}

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

    private String rcpaPreferredTerm;
    private Set<String> rcpaSynonyms;
    private String usageGuidance;
    private Double length;
    private String specimen;
    private String snomedCode;
    private Double version;
    private String history;

    public SnomedRefsetEntry() {
    }

    public Optional<String> getRcpaPreferredTerm() {
        return Optional.ofNullable(rcpaPreferredTerm);
    }

    public void setRcpaPreferredTerm(Optional<String> rcpaPreferredTerm) {
        this.rcpaPreferredTerm = rcpaPreferredTerm.orElse(null);
    }

    public Set<String> getRcpaSynonyms() {
        return rcpaSynonyms;
    }

    public void setRcpaSynonyms(Set<String> rcpaSynonyms) {
        this.rcpaSynonyms = rcpaSynonyms;
    }

    public Optional<String> getUsageGuidance() {
        return Optional.ofNullable(usageGuidance);
    }

    public void setUsageGuidance(Optional<String> usageGuidance) {
        this.usageGuidance = usageGuidance.orElse(null);
    }

    public Optional<Double> getLength() {
        return Optional.ofNullable(length);
    }

    public void setLength(Optional<Double> length) {
        this.length = length.orElse(null);
    }

    public Optional<String> getSpecimen() {
        return Optional.ofNullable(specimen);
    }

    public void setSpecimen(Optional<String> specimen) {
        this.specimen = specimen.orElse(null);
    }

    public Optional<String> getCode() {
        return Optional.ofNullable(snomedCode);
    }

    public void setCode(Optional<String> code) {
        this.snomedCode = code.orElse(null);
    }

    public Optional<Double> getVersion() {
        return Optional.ofNullable(version);
    }

    public void setVersion(Optional<Double> version) {
        this.version = version.orElse(null);
    }

    public Optional<String> getHistory() {
        return Optional.ofNullable(history);
    }

    public void setHistory(Optional<String> history) {
        this.history = history.orElse(null);
    }

}

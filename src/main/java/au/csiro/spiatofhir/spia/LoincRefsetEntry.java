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
public class LoincRefsetEntry implements RefsetEntry {

    private Optional<String> rcpaPreferredTerm;
    private Set<String> rcpaSynonyms;
    private Optional<String> usageGuidance;
    private Optional<Double> length;
    private Optional<String> specimen;
    private Optional<String> unit;
    private Optional<String> ucum;
    private Optional<String> loincCode;
    private Optional<String> loincComponent;
    private Optional<String> loincProperty;
    private Optional<String> loincTiming;
    private Optional<String> loincSystem;
    private Optional<String> loincScale;
    private Optional<String> loincMethod;
    private Optional<String> loincLongName;
    private Optional<Double> version;
    private Optional<String> history;

    public LoincRefsetEntry() {
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

    public Optional<String> getUnit() {
        return unit;
    }

    public void setUnit(Optional<String> unit) {
        this.unit = unit;
    }

    public Optional<String> getUcum() {
        return ucum;
    }

    public void setUcum(Optional<String> ucum) {
        this.ucum = ucum;
    }

    public Optional<String> getCode() {
        return loincCode;
    }

    public void setCode(Optional<String> code) {
        this.loincCode = code;
    }

    public Optional<String> getLoincComponent() {
        return loincComponent;
    }

    public void setLoincComponent(Optional<String> loincComponent) {
        this.loincComponent = loincComponent;
    }

    public Optional<String> getLoincProperty() {
        return loincProperty;
    }

    public void setLoincProperty(Optional<String> loincProperty) {
        this.loincProperty = loincProperty;
    }

    public Optional<String> getLoincTiming() {
        return loincTiming;
    }

    public void setLoincTiming(Optional<String> loincTiming) {
        this.loincTiming = loincTiming;
    }

    public Optional<String> getLoincSystem() {
        return loincSystem;
    }

    public void setLoincSystem(Optional<String> loincSystem) {
        this.loincSystem = loincSystem;
    }

    public Optional<String> getLoincScale() {
        return loincScale;
    }

    public void setLoincScale(Optional<String> loincScale) {
        this.loincScale = loincScale;
    }

    public Optional<String> getLoincMethod() {
        return loincMethod;
    }

    public void setLoincMethod(Optional<String> loincMethod) {
        this.loincMethod = loincMethod;
    }

    public Optional<String> getLoincLongName() {
        return loincLongName;
    }

    public void setLoincLongName(Optional<String> loincLongName) {
        this.loincLongName = loincLongName;
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

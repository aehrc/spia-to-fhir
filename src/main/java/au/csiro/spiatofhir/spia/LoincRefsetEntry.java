/*
 *    Copyright 2018 Australian e-Health Research Centre, CSIRO
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

package au.csiro.spiatofhir.spia;

import java.util.Optional;
import java.util.Set;

/**
 * @author John Grimes
 */
public class LoincRefsetEntry implements RefsetEntry {

    private String rcpaPreferredTerm;
    private Set<String> rcpaSynonyms;
    private String usageGuidance;
    private Double length;
    private String specimen;
    private String unit;
    private String ucum;
    private String loincCode;
    private String loincComponent;
    private String loincProperty;
    private String loincTiming;
    private String loincSystem;
    private String loincScale;
    private String loincMethod;
    private String loincLongName;
    private Double version;
    private String history;

    public LoincRefsetEntry() {
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

    public Optional<String> getUnit() {
        return Optional.ofNullable(unit);
    }

    public void setUnit(Optional<String> unit) {
        this.unit = unit.orElse(null);
    }

    public Optional<String> getUcum() {
        return Optional.ofNullable(ucum);
    }

    public void setUcum(Optional<String> ucum) {
        this.ucum = ucum.orElse(null);
    }

    public Optional<String> getCode() {
        return Optional.ofNullable(loincCode);
    }

    public void setCode(Optional<String> code) {
        this.loincCode = code.orElse(null);
    }

    public Optional<String> getLoincComponent() {
        return Optional.ofNullable(loincComponent);
    }

    public void setLoincComponent(Optional<String> loincComponent) {
        this.loincComponent = loincComponent.orElse(null);
    }

    public Optional<String> getLoincProperty() {
        return Optional.ofNullable(loincProperty);
    }

    public void setLoincProperty(Optional<String> loincProperty) {
        this.loincProperty = loincProperty.orElse(null);
    }

    public Optional<String> getLoincTiming() {
        return Optional.ofNullable(loincTiming);
    }

    public void setLoincTiming(Optional<String> loincTiming) {
        this.loincTiming = loincTiming.orElse(null);
    }

    public Optional<String> getLoincSystem() {
        return Optional.ofNullable(loincSystem);
    }

    public void setLoincSystem(Optional<String> loincSystem) {
        this.loincSystem = loincSystem.orElse(null);
    }

    public Optional<String> getLoincScale() {
        return Optional.ofNullable(loincScale);
    }

    public void setLoincScale(Optional<String> loincScale) {
        this.loincScale = loincScale.orElse(null);
    }

    public Optional<String> getLoincMethod() {
        return Optional.ofNullable(loincMethod);
    }

    public void setLoincMethod(Optional<String> loincMethod) {
        this.loincMethod = loincMethod.orElse(null);
    }

    public Optional<String> getLoincLongName() {
        return Optional.ofNullable(loincLongName);
    }

    public void setLoincLongName(Optional<String> loincLongName) {
        this.loincLongName = loincLongName.orElse(null);
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

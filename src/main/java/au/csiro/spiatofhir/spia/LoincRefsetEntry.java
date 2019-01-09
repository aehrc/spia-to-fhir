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
    private String rcpaUnit;
    private String ucumCode;
    private String ucumDisplay;
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

    @Override
    public String getNativeDisplay() {
        return loincLongName;
    }

    @Override
    public String getCode() {
        return loincCode;
    }

    @Override
    public String getRcpaPreferredTerm() {
        return rcpaPreferredTerm;
    }

    public void setRcpaPreferredTerm(String rcpaPreferredTerm) {
        this.rcpaPreferredTerm = rcpaPreferredTerm;
    }

    @Override
    public Set<String> getRcpaSynonyms() {
        return rcpaSynonyms;
    }

    public void setRcpaSynonyms(Set<String> rcpaSynonyms) {
        this.rcpaSynonyms = rcpaSynonyms;
    }

    public String getUsageGuidance() {
        return usageGuidance;
    }

    public void setUsageGuidance(String usageGuidance) {
        this.usageGuidance = usageGuidance;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }

    public String getRcpaUnit() {
        return rcpaUnit;
    }

    public void setRcpaUnit(String rcpaUnit) {
        this.rcpaUnit = rcpaUnit;
    }

    public String getUcumCode() {
        return ucumCode;
    }

    public void setUcumCode(String ucumCode) {
        this.ucumCode = ucumCode;
    }

    public String getUcumDisplay() {
        return ucumDisplay;
    }

    public void setUcumDisplay(String ucumDisplay) {
        this.ucumDisplay = ucumDisplay;
    }

    public String getLoincCode() {
        return loincCode;
    }

    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }

    public String getLoincComponent() {
        return loincComponent;
    }

    public void setLoincComponent(String loincComponent) {
        this.loincComponent = loincComponent;
    }

    public String getLoincProperty() {
        return loincProperty;
    }

    public void setLoincProperty(String loincProperty) {
        this.loincProperty = loincProperty;
    }

    public String getLoincTiming() {
        return loincTiming;
    }

    public void setLoincTiming(String loincTiming) {
        this.loincTiming = loincTiming;
    }

    public String getLoincSystem() {
        return loincSystem;
    }

    public void setLoincSystem(String loincSystem) {
        this.loincSystem = loincSystem;
    }

    public String getLoincScale() {
        return loincScale;
    }

    public void setLoincScale(String loincScale) {
        this.loincScale = loincScale;
    }

    public String getLoincMethod() {
        return loincMethod;
    }

    public void setLoincMethod(String loincMethod) {
        this.loincMethod = loincMethod;
    }

    public String getLoincLongName() {
        return loincLongName;
    }

    public void setLoincLongName(String loincLongName) {
        this.loincLongName = loincLongName;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

}

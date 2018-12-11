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

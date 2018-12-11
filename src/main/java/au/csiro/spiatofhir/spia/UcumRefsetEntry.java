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

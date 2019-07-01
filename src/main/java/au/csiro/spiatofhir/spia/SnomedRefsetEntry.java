/*
 * Copyright 2019 Australian e-Health Research Centre, CSIRO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.csiro.spiatofhir.spia;

import java.util.Set;

/**
 * @author John Grimes
 */
public class SnomedRefsetEntry implements RefsetEntry {

  private String snomedPreferredTerm;
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

  @Override
  public String getNativeDisplay() {
    return snomedPreferredTerm;
  }

  @Override
  public String getCode() {
    return snomedCode;
  }

  public String getSnomedPreferredTerm() {
    return snomedPreferredTerm;
  }

  public void setSnomedPreferredTerm(String snomedPreferredTerm) {
    this.snomedPreferredTerm = snomedPreferredTerm;
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

  public String getSnomedCode() {
    return snomedCode;
  }

  public void setSnomedCode(String snomedCode) {
    this.snomedCode = snomedCode;
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

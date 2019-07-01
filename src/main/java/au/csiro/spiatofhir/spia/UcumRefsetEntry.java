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

import java.util.HashSet;
import java.util.Set;

/**
 * @author John Grimes
 */
public class UcumRefsetEntry implements RefsetEntry {

  private String rcpaPreferredTerm;
  private String description;
  private String ucumCode;
  private String ucumDisplay;

  public UcumRefsetEntry() {
  }

  @Override
  public String getNativeDisplay() {
    return ucumDisplay;
  }

  @Override
  public Set<String> getRcpaSynonyms() {
    return new HashSet<>();
  }

  @Override
  public String getCode() {
    return ucumCode;
  }

  @Override
  public String getRcpaPreferredTerm() {
    return rcpaPreferredTerm;
  }

  public void setRcpaPreferredTerm(String rcpaPreferredTerm) {
    this.rcpaPreferredTerm = rcpaPreferredTerm;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

}

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

package au.csiro.spiatofhir.utils;

/**
 * @author John Grimes
 */
public abstract class Strings {

  /**
   * This trims whitespace from strings, including non-breaking spaces.
   */
  public static String trim(String subject) {
    return subject.replace('\u00A0', ' ').trim();
  }

  /**
   * This retrieves the major component from a semantic version number string.
   */
  public static String majorVersionFromSemVer(String semVer) {
    return semVer.split("\\.")[0];
  }

}

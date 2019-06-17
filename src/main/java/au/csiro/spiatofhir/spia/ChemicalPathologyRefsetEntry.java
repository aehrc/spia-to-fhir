/*
 *    Copyright 2019 Australian e-Health Research Centre, CSIRO
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

/**
 * @author John Grimes
 */
public class ChemicalPathologyRefsetEntry extends LoincRefsetEntry {

    private CombiningResultsFlag combiningResultsFlag;

    public CombiningResultsFlag getCombiningResultsFlag(){
        return combiningResultsFlag;
    }

    public void setCombiningResultsFlag(CombiningResultsFlag combiningResultsFlag) {
        this.combiningResultsFlag = combiningResultsFlag;
    }

    public enum CombiningResultsFlag {
        RED ("red"), GREEN ("green"), ORANGE ("orange");

        private final String code;

        CombiningResultsFlag(String code) {
           this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

}

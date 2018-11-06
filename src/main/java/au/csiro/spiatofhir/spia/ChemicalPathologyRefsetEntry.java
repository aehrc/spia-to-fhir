/*
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */
package au.csiro.spiatofhir.spia;

import java.util.Optional;

/**
 * @author John Grimes
 */
public class ChemicalPathologyRefsetEntry extends LoincRefsetEntry {

    private Optional<CombiningResultsFlag> combiningResultsFlag;

    public Optional<CombiningResultsFlag> getCombiningResultsFlag() {
        return combiningResultsFlag;
    }

    public void setCombiningResultsFlag(
            Optional<CombiningResultsFlag> combiningResultsFlag) {
        this.combiningResultsFlag = combiningResultsFlag;
    }

    public enum CombiningResultsFlag {
        RED, GREEN, ORANGE
    }

}

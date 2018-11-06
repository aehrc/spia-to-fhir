/*
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com). All rights reserved. Use is subject to
 * license terms and conditions.
 */

package au.csiro.spiatofhir.spia;

import java.util.List;

/**
 * @author John Grimes
 */
public interface HasLoincRefsetEntries {

    List<LoincRefsetEntry> getRefsetEntries();

}

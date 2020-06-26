/*
 * Copyright 2020 Australian e-Health Research Centre, CSIRO
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

package au.csiro.spiatofhir.fhir.r4;

import au.csiro.spiatofhir.spia.Refset;
import java.util.Date;
import org.hl7.fhir.r4.model.Resource;

/**
 * @author John Grimes
 */
public abstract class R4Resource {

  private final Date publicationDate;

  public R4Resource(Date publicationDate) {
    this.publicationDate = publicationDate;
  }

  public abstract Resource transform(Refset refset);

  public Date getPublicationDate() {
    return publicationDate;
  }

}

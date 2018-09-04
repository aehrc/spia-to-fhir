package au.csiro.spiatofhir.spia;

import java.util.Optional;
import java.util.Set;

public interface RefsetEntry {

    public Optional<String> getRcpaPreferredTerm();

    public Set<String> getRcpaSynonyms();

    public Optional<String> getCode();

}

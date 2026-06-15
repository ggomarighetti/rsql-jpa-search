package io.github.ggomarighetti.searchhelper.definition;

import io.github.ggomarighetti.searchhelper.policy.SearchPolicy;
import java.util.Objects;

/** Creates definition builders with application-wide path limits. */
public final class SearchDefinitionFactory {
    private final SearchPolicy policy;

    /**
     * Creates a factory.
     *
     * @param policy application-wide policy
     */
    public SearchDefinitionFactory(SearchPolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy must not be null");
    }

    /**
     * Creates a definition builder.
     *
     * @return definition builder initialized with this factory's policy
     */
    public SearchDefinition.EntityStep builder() {
        return SearchDefinition.builder(policy);
    }

    /**
     * Returns the factory policy.
     *
     * @return immutable application-wide policy
     */
    public SearchPolicy policy() {
        return policy;
    }
}

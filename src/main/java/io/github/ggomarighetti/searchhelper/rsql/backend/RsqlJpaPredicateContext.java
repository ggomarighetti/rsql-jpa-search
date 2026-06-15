package io.github.ggomarighetti.searchhelper.rsql.backend;

import io.github.ggomarighetti.searchhelper.rsql.operator.RsqlOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.metamodel.Attribute;
import java.util.List;
import java.util.Objects;

/** JPA state and converted arguments supplied to a custom operator predicate. */
public final class RsqlJpaPredicateContext {
    private final CriteriaBuilder criteriaBuilder;
    private final Path<?> path;
    private final Attribute<?, ?> attribute;
    private final List<Object> arguments;
    private final From<?, ?> root;
    private final RsqlOperator operator;

    /**
     * Creates a custom-predicate context.
     *
     * @param criteriaBuilder active criteria builder
     * @param path resolved JPA path for the public selector
     * @param attribute terminal metamodel attribute, when available
     * @param arguments immutable converted arguments
     * @param root criteria root or treated subtype root
     * @param operator logical operator being executed
     */
    public RsqlJpaPredicateContext(
            CriteriaBuilder criteriaBuilder,
            Path<?> path,
            Attribute<?, ?> attribute,
            List<Object> arguments,
            From<?, ?> root,
            RsqlOperator operator) {
        this.criteriaBuilder = Objects.requireNonNull(criteriaBuilder, "criteriaBuilder must not be null");
        this.path = Objects.requireNonNull(path, "path must not be null");
        this.attribute = attribute;
        this.arguments = List.copyOf(Objects.requireNonNull(arguments, "arguments must not be null"));
        this.root = Objects.requireNonNull(root, "root must not be null");
        this.operator = Objects.requireNonNull(operator, "operator must not be null");
    }

    /**
     * Returns the active criteria builder.
     *
     * @return active criteria builder
     */
    public CriteriaBuilder criteriaBuilder() {
        return criteriaBuilder;
    }

    /**
     * Returns the resolved selector path.
     *
     * @return resolved path for the selector
     */
    public Path<?> path() {
        return path;
    }

    /**
     * Returns terminal metamodel information.
     *
     * @return terminal metamodel attribute, or {@code null}
     */
    public Attribute<?, ?> attribute() {
        return attribute;
    }

    /**
     * Returns all converted arguments.
     *
     * @return immutable converted argument list
     */
    public List<Object> arguments() {
        return arguments;
    }

    /**
     * Returns the path root.
     *
     * @return criteria root used to resolve the path
     */
    public From<?, ?> root() {
        return root;
    }

    /**
     * Returns the executing operator.
     *
     * @return logical operator being executed
     */
    public RsqlOperator operator() {
        return operator;
    }

    /**
     * Returns one converted argument.
     *
     * @param index zero-based argument index
     * @return converted argument
     */
    public Object argument(int index) {
        return arguments.get(index);
    }
}

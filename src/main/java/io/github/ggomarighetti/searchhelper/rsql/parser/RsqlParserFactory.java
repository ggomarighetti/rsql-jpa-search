package io.github.ggomarighetti.searchhelper.rsql.parser;

import cz.jirutka.rsql.parser.RSQLParser;
import io.github.ggomarighetti.searchhelper.rsql.operator.RsqlOperatorRegistry;

/** Creates parser instances configured with the engine's registered operators. */
@FunctionalInterface
public interface RsqlParserFactory {
    /**
     * Creates a parser for one parse operation.
     *
     * @param operators registered logical and parser operators
     * @return configured parser
     */
    RSQLParser create(RsqlOperatorRegistry operators);
}

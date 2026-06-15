package io.github.ggomarighetti.searchhelper.unit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.ggomarighetti.searchhelper.policy.SearchPolicy;
import io.github.ggomarighetti.searchhelper.rsql.RsqlCompilationRequest;
import io.github.ggomarighetti.searchhelper.rsql.SearchRsqlEngine;
import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.jupiter.api.Test;

class PublicApiSurfaceTest {
    @Test
    void compilationPipelineTypesArePackagePrivate() throws Exception {
        for (String type : List.of(
                "RsqlRulesValidator",
                "RsqlSearchGuard",
                "SearchCompilationMode",
                "SearchPageableGuard",
                "SearchProtectionContext",
                "SearchQueryGuard",
                "SearchSpecificationSorting")) {
            Class<?> implementation =
                    Class.forName("io.github.ggomarighetti.searchhelper.compile." + type);
            assertFalse(Modifier.isPublic(implementation.getModifiers()), type);
        }
        for (String type : List.of(
                "SearchHelperAutoConfiguration",
                "SearchHelperProperties",
                "SearchRsqlAutoConfiguration")) {
            Class<?> implementation =
                    Class.forName("io.github.ggomarighetti.searchhelper.autoconfigure." + type);
            assertFalse(Modifier.isPublic(implementation.getModifiers()), type);
        }
    }

    @Test
    void internalApiMarkerNoLongerExists() {
        assertThrows(
                ClassNotFoundException.class,
                () -> Class.forName("io.github.ggomarighetti.searchhelper.InternalApi"));
    }

    @Test
    void lowLevelRsqlContractsRemainAnExplicitExtensionSpi() throws Exception {
        assertTrue(Modifier.isPublic(
                SearchRsqlEngine.class.getMethod("parse", String.class).getModifiers()));
        assertTrue(Modifier.isPublic(
                SearchRsqlEngine.class.getMethod("compile", RsqlCompilationRequest.class).getModifiers()));
        assertTrue(Modifier.isPublic(
                SearchPolicy.Builder.class.getMethod("buildOverlay").getModifiers()));
    }
}

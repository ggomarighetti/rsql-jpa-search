package io.github.ggomarighetti.searchhelper.integration;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

final class SearchHelperTestAutoConfigurationImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
            "io.github.ggomarighetti.searchhelper.autoconfigure.SearchRsqlAutoConfiguration",
            "io.github.ggomarighetti.searchhelper.autoconfigure.SearchHelperAutoConfiguration"
        };
    }
}

package io.github.ggomarighetti.searchhelper.autoconfigure;

import io.github.ggomarighetti.searchhelper.definition.SearchDefinitionFactory;
import io.github.ggomarighetti.searchhelper.compile.SearchCompiler;
import io.github.ggomarighetti.searchhelper.jpa.JpaSearchDefinitionValidator;
import io.github.ggomarighetti.searchhelper.rsql.SearchRsqlEngine;
import io.github.ggomarighetti.searchhelper.validation.SearchDefinitionValidator;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(afterName = {
        "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration"
}, after = SearchRsqlAutoConfiguration.class)
@EnableConfigurationProperties(SearchHelperProperties.class)
class SearchHelperAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SearchDefinitionFactory searchDefinitionFactory(SearchHelperProperties properties) {
        return new SearchDefinitionFactory(properties.toPolicy());
    }

    @Bean
    @ConditionalOnBean(EntityManagerFactory.class)
    @ConditionalOnMissingBean
    public JpaSearchDefinitionValidator jpaSearchDefinitionValidator(EntityManagerFactory entityManagerFactory) {
        return new JpaSearchDefinitionValidator(entityManagerFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public SearchCompiler searchCompiler(
            SearchRsqlEngine engine,
            ObjectProvider<SearchDefinitionValidator> definitionValidators,
            SearchHelperProperties properties) {
        return new SearchCompiler(
                engine,
                properties.toPolicy(),
                definitionValidators.orderedStream().toList());
    }
}

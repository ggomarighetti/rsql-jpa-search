package io.github.ggomarighetti.searchhelper.integration;

import io.github.ggomarighetti.searchhelper.integration.inheritance.dao.PersonRepository;
import io.github.ggomarighetti.searchhelper.integration.inheritance.domain.Person;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = PersonRepository.class)
@EntityScan(basePackageClasses = Person.class)
@Import(SearchHelperTestAutoConfigurationImportSelector.class)
class PersonSubtypeSearchTestApplication {
}

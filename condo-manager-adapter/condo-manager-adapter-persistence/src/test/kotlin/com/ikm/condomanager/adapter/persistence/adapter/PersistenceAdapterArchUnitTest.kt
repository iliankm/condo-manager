package com.ikm.condomanager.adapter.persistence.adapter

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Arch Unit test for the persistence adapters.
 */
@AnalyzeClasses(
    packages = ["com.ikm.condomanager.adapter.persistence"],
    importOptions = [DoNotIncludeTests::class]
)
class PersistenceAdapterArchUnitTest {
    @ArchTest
    fun `all persistence adapters should be annotated with Component and Transactional`(importedClasses: JavaClasses) {
        classes()
            .that()
            .resideInAPackage("..adapter.persistence.adapter..")
            .should()
            .beAnnotatedWith(Component::class.java)
            .andShould()
            .beAnnotatedWith(Transactional::class.java)
            .check(importedClasses)
    }
}

package com.ikm.condomanager.application

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.properties.HasName
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@AnalyzeClasses(packages = ["com.ikm.condomanager.application"], importOptions = [DoNotIncludeTests::class])
class ServiceArchUnitTest {

    @ArchTest
    fun `all classes implementing UseCase should be annotated with @Service`(importedClasses: JavaClasses) {
        classes().that()
            .implement(HasName.Predicates.nameEndingWith("UseCase"))
            .should()
            .haveSimpleNameEndingWith("Service")
            .andShould()
            .beAnnotatedWith(Service::class.java)
            .check(importedClasses)
    }

    @ArchTest
    fun `all classes with names Component should be annotated with @Component`(importedClasses: JavaClasses) {
        classes().that()
            .haveSimpleNameEndingWith("Component")
            .should()
            .beAnnotatedWith(Component::class.java)
            .allowEmptyShould(true)
            .check(importedClasses)
    }
}

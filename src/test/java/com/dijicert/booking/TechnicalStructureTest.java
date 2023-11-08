package com.dijicert.booking;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.belongToAnyOf;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = BookingApplication.class, importOptions = DoNotIncludeTests.class)
class TechnicalStructureTest {

    @ArchTest
    static final ArchRule respectsTechnicalArchitectureLayers = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Config").definedBy("..config..")
            .layer("Controller").definedBy("..controller..")
            .optionalLayer("Service").definedBy("..service..")
            .optionalLayer("Repository").definedBy("..repository..")
            .layer("Domain").definedBy("..domain..")
            .layer("Error").definedBy("..error..")
            .layer("Enum").definedBy("..enumeration..")

            .whereLayer("Config").mayNotBeAccessedByAnyLayer()
            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Enum").mayOnlyBeAccessedByLayers("Domain", "Service")
            .whereLayer("Error").mayOnlyBeAccessedByLayers("Controller", "Service", "Error")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Repository", "Service", "Controller")

            .ignoreDependency(belongToAnyOf(BookingApplication.class), alwaysTrue());
}

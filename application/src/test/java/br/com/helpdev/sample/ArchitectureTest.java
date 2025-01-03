package br.com.helpdev.sample;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.Architectures;

/**
 * This class is responsible for testing the architecture of the application.
 * ---
 * The architecture is based on the hexagonal architecture pattern.
 * The hexagonal architecture pattern is a way to structure your application to make it more testable, maintainable, and flexible.
 * Don't change the tests in this class, they are used to validate the architecture of the application.
 * If you want to change make sure you understand the consequences.
 * ---
 */
@DisplayName("Architecture Tests")
class ArchitectureTest {

   private static final String CORE_LAYER = "Core";

   private static final String ADAPTERS_LAYER = "Adapters";

   private static final String CONFIG_LAYER = "Config";

   private static final String CORE_PORTS_INPUT_LAYER = "Core Ports Input";

   private static final String CORE_PORTS_OUTPUT_LAYER = "Core Ports Output";

   private static final String CORE_USE_CASES_LAYER = "Core Use Cases";

   private static final String CORE_DOMAIN_LAYER = "Core Domain";

   private static final String ADAPTER_INPUT_LAYER = "Adapter Input";

   private static final String ADAPTER_OUTPUT_LAYER = "Adapter Output";

   /**
    * Get the base package of the class.
    * If you move this class to another package, you need to fix this method to return the correct package code.
    *
    * @return The base package of the class.
    */
   private String getBasePackage() {
      return this.getClass().getPackageName();
   }

   @Test
   @DisplayName("The layers should have the correct dependencies. The Core layer should only be accessed by the Config and Adapters layers. The "
         + "Adapters layer should only be accessed by the Config layer. The Config layer should not be accessed by any layer.")
   void hexagonalArchitectureLayersHaveCorrectDependencies() {
      String basePackage = getBasePackage();
      JavaClasses importedClasses = new ClassFileImporter().importPackages(basePackage);

      final var hexagonalArchitecture = Architectures
            .layeredArchitecture()
            .consideringOnlyDependenciesInLayers()

            .layer(CORE_LAYER)
            .definedBy(basePackage + ".core..")
            .layer(ADAPTERS_LAYER)
            .definedBy(basePackage + ".adapters..")
            .layer(CONFIG_LAYER)
            .definedBy(basePackage + ".config..")

            .whereLayer(CONFIG_LAYER)
            .mayNotBeAccessedByAnyLayer()
            .whereLayer(ADAPTERS_LAYER)
            .mayOnlyBeAccessedByLayers(CONFIG_LAYER)
            .whereLayer(CORE_LAYER)
            .mayOnlyBeAccessedByLayers(CONFIG_LAYER, ADAPTERS_LAYER)
            .allowEmptyShould(true);

      hexagonalArchitecture.check(importedClasses);
   }

   @Test
   @DisplayName("Check the details of the dependencies between the layers. All details should be correct.")
   void hexagonalArchitectureLayersHaveCorrectDependenciesDetails() {
      String basePackage = getBasePackage();
      JavaClasses importedClasses = new ClassFileImporter().importPackages(basePackage);

      final var detailedArchitecture = Architectures
            .layeredArchitecture()
            .consideringOnlyDependenciesInLayers()

            .layer(CORE_PORTS_INPUT_LAYER)
            .definedBy(basePackage + ".core.ports.input..")
            .layer(CORE_PORTS_OUTPUT_LAYER)
            .definedBy(basePackage + ".core.ports.output..")
            .layer(CORE_USE_CASES_LAYER)
            .definedBy(basePackage + ".core.usecases..")
            .layer(CORE_DOMAIN_LAYER)
            .definedBy(basePackage + ".core.domain..")

            .layer(ADAPTER_INPUT_LAYER)
            .definedBy(basePackage + ".adapter.input..")
            .layer(ADAPTER_OUTPUT_LAYER)
            .definedBy(basePackage + ".adapter.output..")

            .layer(CONFIG_LAYER)
            .definedBy(basePackage + ".config..")

            .whereLayer(CONFIG_LAYER)
            .mayNotBeAccessedByAnyLayer()

            .whereLayer(ADAPTER_INPUT_LAYER)
            .mayOnlyBeAccessedByLayers(CONFIG_LAYER)

            .whereLayer(ADAPTER_OUTPUT_LAYER)
            .mayOnlyBeAccessedByLayers(CONFIG_LAYER)

            .whereLayer(CORE_USE_CASES_LAYER)
            .mayOnlyBeAccessedByLayers(CONFIG_LAYER)

            .whereLayer(CORE_PORTS_INPUT_LAYER)
            .mayOnlyBeAccessedByLayers(CONFIG_LAYER, CORE_USE_CASES_LAYER, ADAPTER_INPUT_LAYER)

            .whereLayer(CORE_PORTS_OUTPUT_LAYER)
            .mayOnlyBeAccessedByLayers(CONFIG_LAYER, CORE_USE_CASES_LAYER, ADAPTER_OUTPUT_LAYER)

            .whereLayer(CORE_DOMAIN_LAYER)
            .mayOnlyBeAccessedByLayers(CONFIG_LAYER, CORE_USE_CASES_LAYER, CORE_PORTS_INPUT_LAYER, CORE_PORTS_OUTPUT_LAYER, ADAPTER_INPUT_LAYER,
                  ADAPTER_OUTPUT_LAYER)

            .allowEmptyShould(true);
      detailedArchitecture.check(importedClasses);
   }

   @Test
   @DisplayName("The ports should be interfaces and end with Port. It is a good practice to use the suffix Port to identify the ports.")
   void portsShouldBeInterfacesAndEndWithPort() {
      String basePackage = getBasePackage();
      JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(basePackage);

      classes()
            .that()
            .resideInAnyPackage(basePackage + ".core.ports..")
            .should()
            .beInterfaces()
            .andShould()
            .haveSimpleNameEndingWith("Port")
            .check(importedClasses);
   }

   @Test
   @DisplayName("The classes that implement any Port interface should be package-private. Remove the public modifier from the class and constructor"
         + ". Its to guarantee that implementation details are hidden.")
   void everyClassThatImplementAnyPortShouldBePackagePrivate() {
      String basePackage = getBasePackage();
      JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(basePackage);

      classes().that(new DescribedPredicate<JavaClass>("implement any Port interface") {

         @Override
         public boolean test(JavaClass javaClass) {
            return javaClass.getInterfaces().stream().anyMatch(i -> i.getName().endsWith("Port"));
         }
      }).should().bePackagePrivate().check(importedClasses);
   }

   @Test
   @DisplayName("The use cases should implement the input ports and be package-private and end with UseCase; Remove the public modifier from the "
         + "class and constructor.")
   void useCasesShouldImplementInputPortsAndBePackagePrivateAndEndWithUseCase() {
      String basePackage = getBasePackage();

      JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(basePackage);

      classes()
            .that()
            .resideInAnyPackage(basePackage + ".core.usecases..")
            .should()
            .haveSimpleNameEndingWith("UseCase")
            .andShould(shouldImplementAnyClassInPackage(basePackage + ".core.ports.input"))
            .check(importedClasses);
   }

   @Test
   @DisplayName("One use case should not use another use case or input port because it is a violation of the hexagonal architecture pattern with "
         + "cross dependencies. Instead, use a domain service.")
   void shouldNotUseCaseUsageOtherUseCase() {
      String basePackage = getBasePackage();

      JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(basePackage);

      classes().that().resideInAnyPackage(basePackage + ".core.usecases..").should(new ArchCondition<JavaClass>("not usage other UseCase") {

         @Override
         public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
            javaClass
                  .getFields()
                  .stream()
                  .filter(javaField -> javaField.getRawType().getPackageName().contains(".core.ports.input") || javaField
                        .getRawType()
                        .getPackageName()
                        .contains(".core.usecases"))
                  .forEach(javaField -> {
                     conditionEvents.add(SimpleConditionEvent.violated(javaClass, "The " + javaClass.getName() + " should not use other UseCase"));
                  });
         }
      }).check(importedClasses);
   }

   private static ArchCondition<JavaClass> shouldImplementAnyClassInPackage(final String basePackage) {
      return new ArchCondition<JavaClass>("implement any class in the package " + basePackage) {

         @Override
         public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
            final var useCaseInterfaces = javaClass.getInterfaces();
            if (useCaseInterfaces.isEmpty()) {
               conditionEvents.add(SimpleConditionEvent.violated(javaClass,
                     "The " + javaClass.getName() + " should implement any class in the package " + basePackage));
               return;
            }
            for (var useCaseInterface : useCaseInterfaces) {
               if (!useCaseInterface.getName().startsWith(basePackage)) {
                  conditionEvents.add(SimpleConditionEvent.violated(javaClass,
                        "The " + javaClass.getName() + " should implement any class in the package " + basePackage));
               }
            }
         }
      };
   }
}

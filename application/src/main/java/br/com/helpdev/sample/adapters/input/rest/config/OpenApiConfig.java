package br.com.helpdev.sample.adapters.input.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

   @Bean
   public OpenAPI customOpenAPI() {
      return new OpenAPI().info(new Info()
            .title("Java Architecture Template")
            .version("1.0.0")
            .description("""
                  The application is designed to adhere to Hexagonal Architecture (also known as Ports and Adapters) or Clean Architecture,
                          ensuring a clear separation of concerns, maintainability, and testability.
                          Additionally, it includes a dedicated acceptance-test module for integration testing to validate the application's behavior
                          from an external perspective, independent of its internal implementation.""")
            .contact(new Contact().name("Guilherme Biff Zarelli").email("contato@helpdev.com.br")));

   }
}

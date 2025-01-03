package br.com.helpdev.acceptance;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import br.com.helpdev.acceptance.containers.DefaultContainerStarter;

class UserEndpointsIT extends DefaultContainerStarter {

   @Test
   void shouldCreateUserWithSuccess() {
      given()
            .body("{\"name\":\"John Doe\",\"email\":\"d9D0r@example.com\", \"birth_date\":\"1990-01-01\"}")
            .contentType("application/json")
            .when()
            .post("/user")
            .then()
            .statusCode(201)
            .header("Location", notNullValue());
   }

   @Test
   void shouldReturnBadRequest_whenNameIsBlank() {
      given()
            .body("{\"name\":\"\",\"email\":\"d9D0r@example.com\", \"birth_date\":\"1990-01-01\"}")
            .contentType("application/json")
            .when()
            .post("/user")
            .then()
            .statusCode(400);
   }

   @Test
   void shouldReturnBadRequest_whenEmailIsInvalid() {
      given()
            .body("{\"name\":\"John Doe\",\"email\":\"invalid_email\", \"birth_date\":\"1990-01-01\"}")
            .contentType("application/json")
            .when()
            .post("/user")
            .then()
            .statusCode(400)
            .body("detail", equalTo("Email is invalid"));
   }

   @Test
   void shouldReturnBadRequest_whenBirthDateIsInvalid() {
      given()
            .body("{\"name\":\"John Doe\",\"email\":\"d9D0r@example.com\", \"birth_date\":\"invalid_date\"}")
            .contentType("application/json")
            .when()
            .post("/user")
            .then()
            .statusCode(400);
   }

   @Test
   void shouldReturnBadRequest_whenUserIsNotMajor() {
      final var birthDate = LocalDate.now().minusYears(1);
      final var birthDateString = birthDate.toString();
      given()
            .body("{\"name\":\"John Doe\",\"email\":\"d9D0r@example.com\", \"birth_date\":\"" + birthDateString + "\"}")
            .contentType("application/json")
            .when()
            .post("/user")
            .then()
            .statusCode(400)
            .body("detail", equalTo("User is not major"));
   }
}
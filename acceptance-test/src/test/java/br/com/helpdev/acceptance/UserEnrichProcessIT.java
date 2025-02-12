package br.com.helpdev.acceptance;

import static br.com.helpdev.acceptance.mock.RandomDataApiMock.mockFailureRandomAddress;
import static br.com.helpdev.acceptance.mock.RandomDataApiMock.mockSuccessRandomAddress;
import static java.util.concurrent.TimeUnit.SECONDS;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.testcontainers.shaded.org.awaitility.Awaitility.waitAtMost;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import br.com.helpdev.acceptance.containers.DefaultContainerStarter;

@Execution(ExecutionMode.SAME_THREAD)
class UserEnrichProcessIT extends DefaultContainerStarter {

   @Test
   void shouldEnrichUserWithSuccess() {
      mockSuccessRandomAddress(MOCK_SERVER);

      final var response = given()
            .body("{\"name\":\"John Doe\",\"email\":\"d9D0r@example.com\", \"birth_date\":\"1990-01-01\"}")
            .contentType("application/json")
            .when()
            .post("/user")
            .then()
            .statusCode(201)
            .header("Location", notNullValue());

      waitAtMost(10, SECONDS).untilAsserted(() -> MOCK_SERVER.verify(1, getRequestedFor(urlPathMatching("/api/v2/addresses"))));

      final var locationValue = response.extract().header("Location");

      waitAtMost(10, SECONDS).untilAsserted(() -> {
         given()
               .when()
               .get(locationValue)
               .then()
               .statusCode(200)
               .body("name", equalTo("John Doe"))
               .body("email", equalTo("d9D0r@example.com"))
               .body("birth_date", equalTo("1990-01-01"))
               .body("address", notNullValue());
      });
   }

   @Test
   void shouldEnrichUserWithFailureCountRetryRequests() {
      mockFailureRandomAddress(MOCK_SERVER);

      final var response = given()
            .body("{\"name\":\"John Doe\",\"email\":\"d9D0r@example.com\", \"birth_date\":\"1990-01-01\"}")
            .contentType("application/json")
            .when()
            .post("/user")
            .then()
            .statusCode(201);

      // 3 client retry for each topic retryable sum 9 requests
      waitAtMost(30, SECONDS).untilAsserted(() -> MOCK_SERVER.verify(9, getRequestedFor(urlPathMatching("/api/v2/addresses"))));

      final var locationValue = response.extract().header("Location");

      given()
            .when()
            .get(locationValue)
            .then()
            .statusCode(200)
            .body("name", equalTo("John Doe"))
            .body("email", equalTo("d9D0r@example.com"))
            .body("birth_date", equalTo("1990-01-01"))
            .body("address", nullValue());
   }

}

package br.com.helpdev.acceptance;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import br.com.helpdev.acceptance.containers.DefaultContainerStarter;

class ManagementActuatorEndpointsIT extends DefaultContainerStarter {

   @Test
   void shouldHealthWithSuccess() {
      given()
            .when()
            .get("/actuator/health")
            .then()
            .body("status", equalTo("UP"))
            .body("components.diskSpace.status", equalTo("UP"))
            .body("components.ping.status", equalTo("UP"))
            .body("components.db.status", equalTo("UP"))
            .body("components.livenessState.status", equalTo("UP"))
            .body("components.readinessState.status", equalTo("UP"))
            .body("groups", contains("liveness", "readiness"))
            .statusCode(200);
   }

   @Test
   void shouldLivenessWithSuccess() {
      given().when().get("/actuator/health/liveness").then().body("status", equalTo("UP")).statusCode(200);
   }

   @Test
   void shouldReadinessWithSuccess() {
      given().when().get("/actuator/health/readiness").then().body("status", equalTo("UP")).statusCode(200);
   }
}

package br.com.helpdev.acceptance.containers;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.slf4j.LoggerFactory;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.lifecycle.Startable;

import com.github.tomakehurst.wiremock.WireMockServer;

public abstract class DefaultContainerStarter {

   private static final GenericContainer<?> APP;

   private static final GenericContainer<?> FLYWAY;

   private static final MySQLContainer<?> MYSQL_CONTAINER;

   private static final KafkaContainer KAFKA_CONTAINER;

   private static final GenericContainer<?> RABBITMQ_CONTAINER;

   private static final Network NETWORK = Network.newNetwork();

   protected static final WireMockServer MOCK_SERVER;

   protected static final String USER_COMPLETED_QUEUE = "user-completed.integration.queue";

   public static final String DOCKER_IMAGE_APP = "application:acceptance-test";

   private static final String RABBITMQ_USERNAME = "user";

   private static final String RABBITMQ_PASSWORD = "password";

   private static final int RABBITMQ_AMQP_PORT = 5672;

   private static final int RABBITMQ_MANAGEMENT_PORT = 15672;

   private static final String RABBITMQ_DEFAULT_VHOST = "%2F";

   /* Containers are initialized in static block to create only once in test execution */
   static {
      MOCK_SERVER = new WireMockServer(wireMockConfig().dynamicPort());
      MOCK_SERVER.start();
      exposeHostMachinePortToContainersForApiIntegrations();

      KAFKA_CONTAINER = buildKafkaContainer();
      KAFKA_CONTAINER.start();

      RABBITMQ_CONTAINER = buildRabbitMqContainer();
      RABBITMQ_CONTAINER.start();

      MYSQL_CONTAINER = buildMySqlContainer();
      MYSQL_CONTAINER.start();

      FLYWAY = buildFlywayContainerFromApp(MYSQL_CONTAINER);
      FLYWAY.start();

      APP = buildAppContainer(MYSQL_CONTAINER, FLYWAY, KAFKA_CONTAINER, RABBITMQ_CONTAINER);
      APP.start();

      initRestAssured();
   }

   private static MySQLContainer<?> buildMySqlContainer() {
      return new MySQLContainer<>("mysql:8.0.28").withNetwork(NETWORK).withNetworkAliases("mysqldb");
   }

   private static GenericContainer<?> buildFlywayContainerFromApp(final Startable... dependsOn) {
      return new GenericContainer<>(DOCKER_IMAGE_APP)
            .dependsOn(dependsOn)
            .withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("sh", "-c"))
            .withNetwork(NETWORK)
            .withCommand("/flyway/run-migration.sh")
            .withEnv("DATABASE_URL", "jdbc:mysql://mysqldb:" + MySQLContainer.MYSQL_PORT + "/test?autoReconnect=true&useSSL=false")
            .withEnv("DATABASE_USER", MYSQL_CONTAINER.getUsername())
            .withEnv("DATABASE_PASSWORD", MYSQL_CONTAINER.getPassword())
            .waitingFor(Wait.forLogMessage("(?s).*No migration necessary(?s).*|(?s).*Successfully applied(?s).*", 1))
             .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("FLYWAY")));
   }

   private static GenericContainer<?> buildRabbitMqContainer() {
      return new GenericContainer<>("rabbitmq:3.13-management")
            .withExposedPorts(RABBITMQ_AMQP_PORT, RABBITMQ_MANAGEMENT_PORT)
            .withEnv("RABBITMQ_DEFAULT_USER", RABBITMQ_USERNAME)
            .withEnv("RABBITMQ_DEFAULT_PASS", RABBITMQ_PASSWORD)
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("RABBITMQ_CONTAINER")))
            .withNetworkAliases("rabbitmq")
            .withNetwork(NETWORK)
            .waitingFor(Wait.forLogMessage(".*Server startup complete.*", 1));
   }

   private static KafkaContainer buildKafkaContainer() {
      return new KafkaContainer("apache/kafka-native:3.8.0")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("KAFKA_CONTAINER")))
            .withListener("kafka:19092")
            .withNetworkAliases("kafka")
            .withNetwork(NETWORK);
   }

   private static GenericContainer<?> buildAppContainer(final Startable... dependsOn) {
      return new GenericContainer<>(DOCKER_IMAGE_APP)
            .dependsOn(dependsOn)
            .withNetwork(NETWORK)
            .withEnv("RANDOM_DATA_API_URL", "http://host.testcontainers.internal:" + MOCK_SERVER.port())
            .withEnv("DATABASE_USER", MYSQL_CONTAINER.getUsername())
            .withEnv("DATABASE_PASSWORD", MYSQL_CONTAINER.getPassword())
            .withEnv("OTEL_EXPORTER_OTLP_METRICS_ENABLED", "false")
             .withEnv("OTEL_EXPORTER_OTLP_TRACES_ENABLED", "false")
             .withEnv("DATABASE_URL", "jdbc:mysql://mysqldb:" + MySQLContainer.MYSQL_PORT + "/test?autoReconnect=true&useSSL=false")
             .withEnv("KAFKA_BOOTSTRAP_SERVERS", "kafka:19092")
             .withEnv("SPRING_RABBITMQ_HOST", "rabbitmq")
             .withEnv("SPRING_RABBITMQ_PORT", Integer.toString(RABBITMQ_AMQP_PORT))
             .withEnv("SPRING_RABBITMQ_USERNAME", RABBITMQ_USERNAME)
             .withEnv("SPRING_RABBITMQ_PASSWORD", RABBITMQ_PASSWORD)
             .withExposedPorts(8080)
             .waitingFor(Wait.forHttp("/actuator/health").forStatusCode(200))
             .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("APP_CONTAINER")));
   }

   private static void initRestAssured() {
      RestAssured.baseURI = "http://" + APP.getHost();
      RestAssured.port = APP.getFirstMappedPort();
      RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
   }

   private static void exposeHostMachinePortToContainersForApiIntegrations() {
      Testcontainers.exposeHostPorts(MOCK_SERVER.port());
   }

   protected static int getRabbitMqQueueMessageCount(final String queueName) {
      final List<Map<String, Object>> messages = given()
            .urlEncodingEnabled(false)
            .baseUri("http://" + RABBITMQ_CONTAINER.getHost())
            .port(RABBITMQ_CONTAINER.getMappedPort(RABBITMQ_MANAGEMENT_PORT))
            .auth()
            .preemptive()
            .basic(RABBITMQ_USERNAME, RABBITMQ_PASSWORD)
            .contentType("application/json")
            .body("{\"count\":10,\"ackmode\":\"ack_requeue_true\",\"encoding\":\"auto\",\"truncate\":50000}")
            .when()
            .post("/api/queues/" + RABBITMQ_DEFAULT_VHOST + "/" + queueName + "/get")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("$");

      return messages.size();
   }

   protected static String getSingleRabbitMqQueueMessage(final String queueName) {
      final List<Map<String, Object>> messages = given()
            .urlEncodingEnabled(false)
            .baseUri("http://" + RABBITMQ_CONTAINER.getHost())
            .port(RABBITMQ_CONTAINER.getMappedPort(RABBITMQ_MANAGEMENT_PORT))
            .auth()
            .preemptive()
            .basic(RABBITMQ_USERNAME, RABBITMQ_PASSWORD)
            .contentType("application/json")
            .body("{\"count\":1,\"ackmode\":\"ack_requeue_false\",\"encoding\":\"auto\",\"truncate\":50000}")
            .when()
            .post("/api/queues/" + RABBITMQ_DEFAULT_VHOST + "/" + queueName + "/get")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("$");

      if (messages.isEmpty()) {
         return null;
      }

      return (String) messages.getFirst().get("payload");
   }

   private static void purgeRabbitMqQueue(final String queueName) {
      given()
            .urlEncodingEnabled(false)
            .baseUri("http://" + RABBITMQ_CONTAINER.getHost())
            .port(RABBITMQ_CONTAINER.getMappedPort(RABBITMQ_MANAGEMENT_PORT))
            .auth()
            .preemptive()
            .basic(RABBITMQ_USERNAME, RABBITMQ_PASSWORD)
            .when()
            .delete("/api/queues/" + RABBITMQ_DEFAULT_VHOST + "/" + queueName + "/contents")
            .then()
            .statusCode(204);
   }

   @AfterEach
   void tearDown() {
      MOCK_SERVER.resetAll();
      purgeRabbitMqQueue(USER_COMPLETED_QUEUE);
      /* add here others resets needed after each test */
   }
}

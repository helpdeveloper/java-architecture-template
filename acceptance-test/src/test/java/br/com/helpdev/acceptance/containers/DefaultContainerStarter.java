package br.com.helpdev.acceptance.containers;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import io.restassured.RestAssured;

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

   private static final Network NETWORK = Network.newNetwork();

   protected static final WireMockServer MOCK_SERVER;

   public static final String DOCKER_IMAGE_APP = "application:acceptance-test";

   /* Containers are initialized in static block to create only once in test execution */
   static {
      MOCK_SERVER = new WireMockServer(wireMockConfig().dynamicPort());
      MOCK_SERVER.start();
      exposeHostMachinePortToContainersForApiIntegrations();

      KAFKA_CONTAINER = buildKafkaContainer();
      KAFKA_CONTAINER.start();

      MYSQL_CONTAINER = buildMySqlContainer();
      MYSQL_CONTAINER.start();

      FLYWAY = buildFlywayContainerFromApp(MYSQL_CONTAINER);
      FLYWAY.start();

      APP = buildAppContainer(MYSQL_CONTAINER, FLYWAY, KAFKA_CONTAINER);
      APP.start();

      initRestAssured();
   }

   private static MySQLContainer<?> buildMySqlContainer() {
      return new MySQLContainer<>("mysql:8.0.28").withNetwork(NETWORK).withNetworkAliases("mysqldb");
   }

   private static GenericContainer<?> buildFlywayContainerFromApp(final Startable... dependsOn) {
      return new GenericContainer<>(DOCKER_IMAGE_APP)
            .dependsOn(dependsOn)
            .withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("/flyway/flyway"))
            .withNetwork(NETWORK)
            .withCommand(
                  "-url=jdbc:mysql://mysqldb:" + MySQLContainer.MYSQL_PORT + "?useSSL=false -schemas=test -user=" + MYSQL_CONTAINER.getUsername()
                        + " -password=" + MYSQL_CONTAINER.getPassword() + " -connectRetries=60 migrate -locations=filesystem:/flyway/sql")
            .waitingFor(Wait.forLogMessage("(?s).*No migration necessary(?s).*|(?s).*Successfully applied(?s).*", 1))
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("FLYWAY")));
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

   @AfterEach
   void tearDown() {
      MOCK_SERVER.resetAll();
      /* add here others resets needed after each test */
   }
}

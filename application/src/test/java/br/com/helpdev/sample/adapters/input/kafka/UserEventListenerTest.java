package br.com.helpdev.sample.adapters.input.kafka;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.format.EventFormat;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;

import br.com.helpdev.sample.adapters.input.kafka.dto.UserEventDataDto;
import br.com.helpdev.sample.core.ports.input.UserEnricherPort;

@ExtendWith(MockitoExtension.class)
class UserEventListenerTest {

   private static final String CLOUD_EVENT_SOURCE = "urn:helpdev:sample:user";

   private static final EventFormat EVENT_FORMAT = Objects.requireNonNull(
         EventFormatProvider.getInstance().resolveFormat(JsonFormat.CONTENT_TYPE));

   @Mock
   private UserEnricherPort userEnricherPort;

   private UserEventListener userEventListener;

   private ObjectMapper objectMapper;

   @BeforeEach
   void setUp() {
      objectMapper = new ObjectMapper();
      userEventListener = new UserEventListener(objectMapper, userEnricherPort);
   }

   @Test
   void testListen_UserCreatedEvent() throws Exception {
      final var userUuid = UUID.randomUUID();
      final var message = cloudEventMessage("br.com.helpdev.sample.user.created", userUuid.toString());

      userEventListener.listen(message);

      verify(userEnricherPort).enrichUser(userUuid);
      verifyNoMoreInteractions(userEnricherPort);
   }

   @Test
   void testListen_UserEventIgnored() throws Exception {
      final var message = cloudEventMessage("br.com.helpdev.sample.user.address.updated", UUID.randomUUID().toString());

      userEventListener.listen(message);

      verifyNoInteractions(userEnricherPort);
   }

   @Test
   void testListen_UserCreatedEventWithoutDataShouldThrowException() {
      final var cloudEvent = CloudEventBuilder
            .v1()
            .withId(UUID.randomUUID().toString())
            .withType("br.com.helpdev.sample.user.created")
            .withSource(URI.create(CLOUD_EVENT_SOURCE))
            .withTime(OffsetDateTime.now())
            .build();

      final var message = new String(EVENT_FORMAT.serialize(cloudEvent), StandardCharsets.UTF_8);

      assertThrows(IllegalArgumentException.class, () -> userEventListener.listen(message));
      verifyNoInteractions(userEnricherPort);
   }

   private String cloudEventMessage(final String eventType, final String userUuid) throws Exception {
      final var cloudEvent = CloudEventBuilder
            .v1()
            .withId(UUID.randomUUID().toString())
            .withType(eventType)
            .withSource(URI.create(CLOUD_EVENT_SOURCE))
            .withTime(OffsetDateTime.now())
            .withData("application/json", objectMapper.writeValueAsBytes(new UserEventDataDto(userUuid)))
            .build();

      return new String(EVENT_FORMAT.serialize(cloudEvent), StandardCharsets.UTF_8);
   }
}

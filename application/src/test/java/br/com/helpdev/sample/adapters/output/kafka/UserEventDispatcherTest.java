package br.com.helpdev.sample.adapters.output.kafka;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cloudevents.SpecVersion;
import io.cloudevents.core.format.EventFormat;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;

import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.vo.Email;

@ExtendWith(MockitoExtension.class)
class UserEventDispatcherTest {

   private static final String CLOUD_EVENT_SOURCE = "urn:helpdev:sample:user";

   private static final EventFormat EVENT_FORMAT = Objects.requireNonNull(
         EventFormatProvider.getInstance().resolveFormat(JsonFormat.CONTENT_TYPE));

   @Mock
   private KafkaTemplate<String, String> kafkaProducer;

   private UserEventDispatcher userEventDispatcher;

   private ObjectMapper objectMapper;

   private User user;

   @BeforeEach
   void setUp() {
      final var userUuid = UUID.randomUUID();
      objectMapper = new ObjectMapper();
      userEventDispatcher = new UserEventDispatcher(kafkaProducer, objectMapper);
      user = new User(1L, userUuid, "John Doe", Email.of("john.doe@example.com"), LocalDate.of(2000, 1, 1), null);
   }

   @Test
   void testSendUserCreatedEvent() throws Exception {
      userEventDispatcher.sendUserCreatedEvent(user);

      assertPublishedEvent("br.com.helpdev.sample.user.created");
   }

   @Test
   void testSendUserAddressUpdatedEvent() throws Exception {
      userEventDispatcher.sendUserAddressUpdatedEvent(user);

      assertPublishedEvent("br.com.helpdev.sample.user.address.updated");
   }

   @Test
   void testSendUserCreatedEvent_WhenSerializationFailsShouldThrowKafkaException() throws Exception {
      final var failingObjectMapper = mock(ObjectMapper.class);
      userEventDispatcher = new UserEventDispatcher(kafkaProducer, failingObjectMapper);

      when(failingObjectMapper.writeValueAsBytes(any(UserEventData.class))).thenThrow(new JsonProcessingException("boom") {
         private static final long serialVersionUID = 1L;
      });

      assertThrows(KafkaException.class, () -> userEventDispatcher.sendUserCreatedEvent(user));
      verifyNoInteractions(kafkaProducer);
   }

   @Test
   void testUserEventRecordShouldExposeCloudEventFields() {
      final var time = OffsetDateTime.now();
      final var userEventData = new UserEventData(user.uuid().toString());
      final var userEvent = new UserEvent("1.0", "event-id", CLOUD_EVENT_SOURCE, "br.com.helpdev.sample.user.created", time,
            "application/json", userEventData);

      assertEquals("1.0", userEvent.specversion());
      assertEquals("event-id", userEvent.id());
      assertEquals(CLOUD_EVENT_SOURCE, userEvent.source());
      assertEquals("br.com.helpdev.sample.user.created", userEvent.type());
      assertEquals(time, userEvent.time());
      assertEquals("application/json", userEvent.datacontenttype());
      assertEquals(user.uuid().toString(), userEvent.data().userUuid());
   }

   private void assertPublishedEvent(final String expectedType) throws Exception {
      final var payloadCaptor = ArgumentCaptor.forClass(String.class);

      verify(kafkaProducer).send(eq("user-events"), eq(user.uuid().toString()), payloadCaptor.capture());
      verifyNoMoreInteractions(kafkaProducer);

      final var cloudEvent = EVENT_FORMAT.deserialize(payloadCaptor.getValue().getBytes(StandardCharsets.UTF_8));

      assertEquals(SpecVersion.V1, cloudEvent.getSpecVersion());
      assertNotNull(cloudEvent.getId());
      assertEquals(expectedType, cloudEvent.getType());
      assertEquals(CLOUD_EVENT_SOURCE, cloudEvent.getSource().toString());
      assertEquals("application/json", cloudEvent.getDataContentType());
      assertNotNull(cloudEvent.getTime());
      assertNotNull(cloudEvent.getData());

      final var data = objectMapper.readValue(cloudEvent.getData().toBytes(), UserEventData.class);
      assertEquals(user.uuid().toString(), data.userUuid());
   }
}

package br.com.helpdev.sample.adapters.output.kafka;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncMessage;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.format.EventFormat;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;

import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.ports.output.UserEventDispatcherPort;

@Service
class UserEventDispatcher implements UserEventDispatcherPort {

   private static final String USER_EVENTS_TOPIC = "user-events";

   private static final String CLOUD_EVENT_SOURCE = "urn:helpdev:sample:user";

   private static final String USER_CREATED_EVENT_TYPE = "br.com.helpdev.sample.user.created";

   private static final String USER_ADDRESS_UPDATED_EVENT_TYPE = "br.com.helpdev.sample.user.address.updated";

   private static final EventFormat EVENT_FORMAT = Objects.requireNonNull(
         EventFormatProvider.getInstance().resolveFormat(JsonFormat.CONTENT_TYPE));

   private final KafkaTemplate<String, String> kafkaProducer;

   private final ObjectMapper objectMapper;

   UserEventDispatcher(final KafkaTemplate<String, String> kafkaProducer, final ObjectMapper objectMapper) {
      this.kafkaProducer = kafkaProducer;
      this.objectMapper = objectMapper;
   }

   @Override
   public void sendUserCreatedEvent(final User user) {
      publish(user, USER_CREATED_EVENT_TYPE);
   }

   @Override
   public void sendUserAddressUpdatedEvent(final User user) {
      publish(user, USER_ADDRESS_UPDATED_EVENT_TYPE);
   }

    @AsyncPublisher(
          operation = @AsyncOperation(
                channelName = USER_EVENTS_TOPIC,
                description = "Publish user events",
                message = @AsyncMessage(
                      name = "UserEvent",
                      contentType = JsonFormat.CONTENT_TYPE,
                      messageId = "id"
                ),
                payloadType = UserEvent.class
          )
    )
    @KafkaAsyncOperationBinding(bindingVersion = "1.0.0")
    void publish(final User user, final String eventType) {
       final var cloudEvent = CloudEventBuilder
             .v1()
             .withId(UUID.randomUUID().toString())
             .withType(eventType)
             .withSource(URI.create(CLOUD_EVENT_SOURCE))
             .withTime(OffsetDateTime.now(ZoneOffset.UTC))
             .withData("application/json", serializeData(new UserEventData(user.uuid().toString())))
             .build();

       kafkaProducer.send(USER_EVENTS_TOPIC, user.uuid().toString(),
             new String(EVENT_FORMAT.serialize(cloudEvent), StandardCharsets.UTF_8));
    }

   private byte[] serializeData(final UserEventData userEventData) {
      try {
         return objectMapper.writeValueAsBytes(userEventData);
      } catch (JsonProcessingException exception) {
         throw new KafkaException("Cannot serialize CloudEvent user payload", exception);
      }
   }

}

package br.com.helpdev.sample.adapters.input.kafka;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncMessage;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.cloudevents.core.format.EventFormat;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.helpdev.sample.adapters.input.kafka.dto.UserEventDataDto;
import br.com.helpdev.sample.adapters.input.kafka.dto.UserEventDto;
import br.com.helpdev.sample.core.ports.input.UserEnricherPort;

@Controller
class UserEventListener {

   private static final String TOPIC_NAME = "user-events";

   private static final EventFormat EVENT_FORMAT = Objects.requireNonNull(
         EventFormatProvider.getInstance().resolveFormat(JsonFormat.CONTENT_TYPE));

   private final Logger logger = LoggerFactory.getLogger(UserEventListener.class);

   private final ObjectMapper objectMapper;

   private final UserEnricherPort userEnricherPort;

   UserEventListener(final ObjectMapper objectMapper, final UserEnricherPort userEnricherPort) {
      this.objectMapper = objectMapper;
      this.userEnricherPort = userEnricherPort;
   }

   @AsyncListener(operation = @AsyncOperation(
         channelName = TOPIC_NAME,
         description = "Listen for user events",
         message = @AsyncMessage(
               name = "UserEventDto",
                contentType = JsonFormat.CONTENT_TYPE,
                messageId = "id"
         ),
         headers = @AsyncOperation.Headers(
               notUsed = true
         ),
         payloadType = UserEventDto.class
   ))
   @KafkaAsyncOperationBinding(bindingVersion = "1.0.0")
   @KafkaListener(topics = TOPIC_NAME)
   @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 1000, maxDelay = 10000, multiplier = 2), autoCreateTopics = "true")
   public void listen(final String message) throws IOException {
      final var cloudEvent = EVENT_FORMAT.deserialize(message.getBytes(StandardCharsets.UTF_8));

      if (UserEventDto.EVENT_TYPE_CREATED.equals(cloudEvent.getType())) {
         final var eventData = cloudEvent.getData();
         if (eventData == null) {
            throw new IllegalArgumentException("CloudEvent data is required for user events");
         }
         final var userEventData = objectMapper.readValue(eventData.toBytes(), UserEventDataDto.class);

         userEnricherPort.enrichUser(UUID.fromString(userEventData.userUuid()));
         logger.info("User enriched: {}", userEventData.userUuid());
         return;
      }

      logger.info("User event ignored to enrich process: {}", cloudEvent.getType());
   }

}

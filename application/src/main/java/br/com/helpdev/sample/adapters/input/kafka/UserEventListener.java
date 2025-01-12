package br.com.helpdev.sample.adapters.input.kafka;

import java.util.UUID;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncMessage;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.helpdev.sample.adapters.input.kafka.dto.UserEventDto;
import br.com.helpdev.sample.core.ports.input.UserEnricherPort;

@Controller
class UserEventListener {

   private static final String TOPIC_NAME = "user-events";

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
               contentType = "application/json",
               messageId = "uuid"
         ),
         headers = @AsyncOperation.Headers(
               notUsed = true
         ),
         payloadType = UserEventDto.class
   ))
   @KafkaAsyncOperationBinding(bindingVersion = "1.0.0")
   @KafkaListener(topics = TOPIC_NAME)
   @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 1000, maxDelay = 10000, multiplier = 2), autoCreateTopics = "true")
   public void listen(final String message) throws JsonProcessingException {
      final var userEventDto = objectMapper.readValue(message, UserEventDto.class);

      if (UserEventDto.EVENT_CREATED.equals(userEventDto.event())) {
         userEnricherPort.enrichUser(UUID.fromString(userEventDto.uuid()));
         logger.info("User enriched: {}", userEventDto.uuid());
         return;
      }

      logger.info("User event ignored to enrich process: {}", userEventDto.event());
   }

}

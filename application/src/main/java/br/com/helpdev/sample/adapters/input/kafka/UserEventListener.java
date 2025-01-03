package br.com.helpdev.sample.adapters.input.kafka;

import java.util.UUID;

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

   private final Logger logger = LoggerFactory.getLogger(UserEventListener.class);

   private final ObjectMapper objectMapper;

   private final UserEnricherPort userEnricherPort;

   UserEventListener(final ObjectMapper objectMapper, final UserEnricherPort userEnricherPort) {
      this.objectMapper = objectMapper;
      this.userEnricherPort = userEnricherPort;
   }

   @KafkaListener(topics = "user-events")
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

package br.com.helpdev.sample.adapters.output.kafka;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncMessage;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.ports.output.UserEventDispatcherPort;

@Service
class UserEventDispatcher implements UserEventDispatcherPort {

   private static final String USER_EVENTS_TOPIC = "user-events";

   private final KafkaTemplate<String, String> kafkaProducer;

   UserEventDispatcher(final KafkaTemplate<String, String> kafkaProducer) {
      this.kafkaProducer = kafkaProducer;
   }

   @Override
   public void sendUserCreatedEvent(final User user) {
      publish(user, UserEvent.ofCreated(user.uuid()));
   }

   @Override
   public void sendUserAddressUpdatedEvent(final User user) {
      publish(user, UserEvent.ofUpdated(user.uuid()));
   }

   @AsyncPublisher(
         operation = @AsyncOperation(
               channelName = USER_EVENTS_TOPIC,
               description = "Publish user events",
               message = @AsyncMessage(
                     name = "UserEvent",
                     contentType = "application/json",
                     messageId = "uuid"
               ),
               payloadType = UserEvent.class
         )
   )
   @KafkaAsyncOperationBinding(bindingVersion = "1.0.0")
   void publish(User user, UserEvent userEvent) {
      kafkaProducer.send(USER_EVENTS_TOPIC, user.uuid().toString(), userEvent.toJson());
   }

}

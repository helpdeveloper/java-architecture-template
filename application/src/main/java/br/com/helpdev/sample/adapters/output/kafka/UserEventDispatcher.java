package br.com.helpdev.sample.adapters.output.kafka;

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
      kafkaProducer.send(USER_EVENTS_TOPIC, user.uuid().toString(), UserEvent.ofCreated(user.uuid()).toJson());
   }

   @Override
   public void sendUserAddressUpdatedEvent(final User user) {
      kafkaProducer.send(USER_EVENTS_TOPIC, user.uuid().toString(), UserEvent.ofUpdated(user.uuid()).toJson());
   }

}

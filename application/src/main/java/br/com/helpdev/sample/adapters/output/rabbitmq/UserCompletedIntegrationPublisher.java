package br.com.helpdev.sample.adapters.output.rabbitmq;

import io.github.springwolf.bindings.amqp.annotations.AmqpAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncMessage;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.ports.output.UserCompletedIntegrationPublisherPort;

@Service
class UserCompletedIntegrationPublisher implements UserCompletedIntegrationPublisherPort {

   private static final String USER_COMPLETED_CHANNEL = "user-completed.integration.queue";

   private final RabbitTemplate rabbitTemplate;

   private final String exchange;

   private final String routingKey;

   UserCompletedIntegrationPublisher(
         final RabbitTemplate rabbitTemplate,
         @Value("${integration.rabbitmq.user-completed.exchange:user-completed.integration.exchange}") final String exchange,
         @Value("${integration.rabbitmq.user-completed.routing-key:user.completed}") final String routingKey) {
      this.rabbitTemplate = rabbitTemplate;
      this.exchange = exchange;
      this.routingKey = routingKey;
   }

   @Override
   public void sendUserCompletedPayload(final User user) {
      publish(UserCompletedPayload.of(user));
   }

   @AsyncPublisher(
         operation = @AsyncOperation(
               channelName = USER_COMPLETED_CHANNEL,
               description = "Publish completed user payloads for integration consumers",
               message = @AsyncMessage(
                     name = "UserCompletedPayload",
                     contentType = "application/json",
                     messageId = "uuid"
               ),
               payloadType = UserCompletedPayload.class
         )
   )
   @AmqpAsyncOperationBinding
   void publish(final UserCompletedPayload payload) {
      rabbitTemplate.convertAndSend(exchange, routingKey, payload);
   }

}

package br.com.helpdev.sample.adapters.output.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
class UserCompletedRabbitMqConfig {

   @Bean
   DirectExchange userCompletedExchange(
         @Value("${integration.rabbitmq.user-completed.exchange:user-completed.integration.exchange}") final String exchangeName) {
      return ExchangeBuilder.directExchange(exchangeName).durable(true).build();
   }

   @Bean
   Queue userCompletedQueue(@Value("${integration.rabbitmq.user-completed.queue:user-completed.integration.queue}") final String queueName) {
      return QueueBuilder.durable(queueName).build();
   }

   @Bean
   Binding userCompletedBinding(
         final Queue userCompletedQueue,
         final DirectExchange userCompletedExchange,
         @Value("${integration.rabbitmq.user-completed.routing-key:user.completed}") final String routingKey) {
      return BindingBuilder.bind(userCompletedQueue).to(userCompletedExchange).with(routingKey);
   }

   @Bean
   MessageConverter rabbitMessageConverter(final ObjectMapper objectMapper) {
      return new Jackson2JsonMessageConverter(objectMapper);
   }

}

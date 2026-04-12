package br.com.helpdev.sample.adapters.output.rabbitmq;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import br.com.helpdev.sample.core.domain.entities.Address;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.vo.Email;

@ExtendWith(MockitoExtension.class)
class UserCompletedIntegrationPublisherTest {

   private static final String EXCHANGE = "user-completed.integration.exchange";

   private static final String ROUTING_KEY = "user.completed";

   @Mock
   private RabbitTemplate rabbitTemplate;

   private UserCompletedIntegrationPublisher userCompletedIntegrationPublisher;

   private User user;

   @BeforeEach
   void setUp() {
      userCompletedIntegrationPublisher = new UserCompletedIntegrationPublisher(rabbitTemplate, EXCHANGE, ROUTING_KEY);
      final var userUuid = UUID.randomUUID();
      final var address = new Address(1L, "Reunion", "Arizona", "East Nadia", "39781-7908", "849 Langosh Ports");
      user = new User(1L, userUuid, "John Doe", Email.of("john.doe@example.com"), LocalDate.of(2000, 1, 1), address);
   }

   @Test
   void sendUserCompletedPayload_shouldPublishCompletedUserPayload() {
      userCompletedIntegrationPublisher.sendUserCompletedPayload(user);

      verify(rabbitTemplate).convertAndSend(EXCHANGE, ROUTING_KEY, UserCompletedPayload.of(user));
      verifyNoMoreInteractions(rabbitTemplate);
   }

}

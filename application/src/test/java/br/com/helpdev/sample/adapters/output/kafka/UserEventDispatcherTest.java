package br.com.helpdev.sample.adapters.output.kafka;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.vo.Email;

@ExtendWith(MockitoExtension.class)
class UserEventDispatcherTest {

   @Mock
   private KafkaTemplate<String, String> kafkaProducer;

   @InjectMocks
   private UserEventDispatcher userEventDispatcher;

   private User user;

   @BeforeEach
   void setUp() {
      final var userUuid = UUID.randomUUID();
      user = new User(1L, userUuid, "John Doe", Email.of("john.doe@example.com"), LocalDate.of(2000, 1, 1), null);
   }

   @Test
   void testSendUserCreatedEvent() {
      userEventDispatcher.sendUserCreatedEvent(user);

      verify(kafkaProducer).send("user-events", user.uuid().toString(), UserEvent.ofCreated(user.uuid()).toJson());
      verifyNoMoreInteractions(kafkaProducer);
   }

   @Test
   void testSendUserAddressUpdatedEvent() {
      userEventDispatcher.sendUserAddressUpdatedEvent(user);

      verify(kafkaProducer).send("user-events", user.uuid().toString(), UserEvent.ofUpdated(user.uuid()).toJson());
      verifyNoMoreInteractions(kafkaProducer);
   }
}
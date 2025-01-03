package br.com.helpdev.sample.adapters.input.kafka;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.helpdev.sample.adapters.input.kafka.dto.UserEventDto;
import br.com.helpdev.sample.core.ports.input.UserEnricherPort;

@ExtendWith(MockitoExtension.class)
class UserEventListenerTest {

   @Mock
   private ObjectMapper objectMapper;

   @Mock
   private UserEnricherPort userEnricherPort;

   @InjectMocks
   private UserEventListener userEventListener;

   @Test
   void testListen_UserCreatedEvent() throws JsonProcessingException {
      UserEventDto userEventDto = new UserEventDto(UserEventDto.EVENT_CREATED, UUID.randomUUID().toString());
      String message = "{\"event\":\"" + UserEventDto.EVENT_CREATED + "\",\"uuid\":\"" + userEventDto.uuid() + "\"}";

      when(objectMapper.readValue(message, UserEventDto.class)).thenReturn(userEventDto);

      userEventListener.listen(message);

      verify(userEnricherPort).enrichUser(UUID.fromString(userEventDto.uuid()));
      verifyNoMoreInteractions(userEnricherPort);
   }

   @Test
   void testListen_UserEventIgnored() throws JsonProcessingException {
      UserEventDto userEventDto = new UserEventDto("EVENT_UPDATED", UUID.randomUUID().toString());
      String message = "{\"event\":\"OTHER_EVENT\",\"uuid\":\"" + userEventDto.uuid() + "\"}";

      when(objectMapper.readValue(message, UserEventDto.class)).thenReturn(userEventDto);

      userEventListener.listen(message);

      verifyNoInteractions(userEnricherPort);
   }
}
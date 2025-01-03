package br.com.helpdev.sample.core.usecases;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.exceptions.UserNotFoundException;
import br.com.helpdev.sample.core.domain.vo.Email;
import br.com.helpdev.sample.core.ports.output.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class UserGetterUseCaseTest {

   @Mock
   private UserRepositoryPort userRepositoryPort;

   @InjectMocks
   private UserGetterUseCase userGetterUseCase;


   @Test
   void testGetUser_Success() {
      UUID uuid = UUID.randomUUID();
      User user = User.of(1L, uuid, "John Doe", Email.of("john.doe@example.com"), LocalDate.of(1990, 1, 1), null);

      when(userRepositoryPort.findByUuid(uuid)).thenReturn(Optional.of(user));

      User result = userGetterUseCase.getUser(uuid);

      assertNotNull(result);
      assertEquals(uuid, result.uuid());
      assertEquals("John Doe", result.name());
      assertEquals("john.doe@example.com", result.email().value());
   }

   @Test
   void testGetUser_UserNotFound() {
      UUID uuid = UUID.randomUUID();
      when(userRepositoryPort.findByUuid(uuid)).thenReturn(Optional.empty());

      assertThrows(UserNotFoundException.class, () -> userGetterUseCase.getUser(uuid));
   }
}
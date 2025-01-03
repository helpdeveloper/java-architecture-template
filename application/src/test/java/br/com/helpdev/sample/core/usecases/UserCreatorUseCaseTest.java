package br.com.helpdev.sample.core.usecases;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.exceptions.UserIsNotMajorException;
import br.com.helpdev.sample.core.domain.vo.Email;
import br.com.helpdev.sample.core.ports.output.UserEventDispatcherPort;
import br.com.helpdev.sample.core.ports.output.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class UserCreatorUseCaseTest {

   @Mock
   private UserRepositoryPort userRepositoryPort;

   @Mock
   private UserEventDispatcherPort userEventDispatcherPort;

   @InjectMocks
   private UserCreatorUseCase userCreatorUseCase;

   @Test
   void createUser_shouldThrowException_whenUserIsNotMajor() {
      User user = User.of("name", Email.of("email@example.com"), LocalDate.now());

      assertThrows(UserIsNotMajorException.class, () -> userCreatorUseCase.createUser(user));
   }

   @Test
   void createUser_shouldSaveUserAndSendEvent_whenUserIsMajor() {
      User user = User.of("name", Email.of("email@example.com"), LocalDate.now().minusYears(User.AGE_OF_MAJORITY + 1));
      User savedUser = user.withId(1L);

      when(userRepositoryPort.save(any(User.class))).thenReturn(savedUser);

      userCreatorUseCase.createUser(user);

      verify(userRepositoryPort).save(user);
      verify(userEventDispatcherPort).sendUserCreatedEvent(savedUser);
   }
}
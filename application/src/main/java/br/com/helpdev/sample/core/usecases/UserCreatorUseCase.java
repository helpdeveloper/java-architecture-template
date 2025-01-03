package br.com.helpdev.sample.core.usecases;

import org.springframework.stereotype.Service;

import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.exceptions.UserIsNotMajorException;
import br.com.helpdev.sample.core.ports.input.UserCreatorPort;
import br.com.helpdev.sample.core.ports.output.UserEventDispatcherPort;
import br.com.helpdev.sample.core.ports.output.UserRepositoryPort;

@Service
class UserCreatorUseCase implements UserCreatorPort {

   private final UserRepositoryPort userRepositoryPort;

   private final UserEventDispatcherPort userEventDispatcherPort;

   UserCreatorUseCase(final UserRepositoryPort userRepositoryPort, final UserEventDispatcherPort userEventDispatcherPort) {
      this.userRepositoryPort = userRepositoryPort;
      this.userEventDispatcherPort = userEventDispatcherPort;
   }

   @Override
   public User createUser(final User user) {
      if (!user.isMajor()) {
         throw new UserIsNotMajorException();
      }
      final var savedUser = userRepositoryPort.save(user);
      userEventDispatcherPort.sendUserCreatedEvent(savedUser);
      return savedUser;
   }

}

package br.com.helpdev.sample.core.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.exceptions.UserNotFoundException;
import br.com.helpdev.sample.core.ports.input.UserGetterPort;
import br.com.helpdev.sample.core.ports.output.UserRepositoryPort;

@Service
class UserGetterUseCase implements UserGetterPort {

   private final UserRepositoryPort userRepositoryPort;

   UserGetterUseCase(final UserRepositoryPort userRepositoryPort) {
      this.userRepositoryPort = userRepositoryPort;
   }

   @Override
   public User getUser(UUID uuid) {
      return userRepositoryPort.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
   }

}

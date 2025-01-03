package br.com.helpdev.sample.core.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.helpdev.sample.core.domain.exceptions.UserNotFoundException;
import br.com.helpdev.sample.core.ports.input.UserEnricherPort;
import br.com.helpdev.sample.core.ports.output.AddressClientPort;
import br.com.helpdev.sample.core.ports.output.AddressRepositoryPort;
import br.com.helpdev.sample.core.ports.output.UserEventDispatcherPort;
import br.com.helpdev.sample.core.ports.output.UserRepositoryPort;

@Service
class UserEnricherUseCase implements UserEnricherPort {

   private final UserRepositoryPort userRepositoryPort;

   private final UserEventDispatcherPort userEventDispatcherPort;

   private final AddressClientPort addressClientPort;

   private final AddressRepositoryPort addressRepositoryPort;

   UserEnricherUseCase(final UserRepositoryPort userRepositoryPort, final UserEventDispatcherPort userEventDispatcherPort,
         final AddressClientPort addressClientPort, final AddressRepositoryPort addressRepositoryPort) {
      this.userRepositoryPort = userRepositoryPort;
      this.userEventDispatcherPort = userEventDispatcherPort;
      this.addressClientPort = addressClientPort;
      this.addressRepositoryPort = addressRepositoryPort;
   }

   @Override
   public void enrichUser(final UUID userUuid) {
      final var user = userRepositoryPort.findByUuid(userUuid).orElseThrow(() -> new UserNotFoundException(userUuid));
      final var address = addressClientPort.findUserAddress(user);
      final var savedAddress = addressRepositoryPort.save(user, address);
      final var userWithAddress = user.withAddress(savedAddress);
      userEventDispatcherPort.sendUserAddressUpdatedEvent(userWithAddress);
   }

}

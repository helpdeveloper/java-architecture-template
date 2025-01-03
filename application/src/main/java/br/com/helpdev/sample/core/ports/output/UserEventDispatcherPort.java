package br.com.helpdev.sample.core.ports.output;

import br.com.helpdev.sample.core.domain.entities.User;

public interface UserEventDispatcherPort {

   void sendUserCreatedEvent(User user);

   void sendUserAddressUpdatedEvent(User user);

}

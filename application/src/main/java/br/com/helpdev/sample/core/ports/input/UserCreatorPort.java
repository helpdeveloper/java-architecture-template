package br.com.helpdev.sample.core.ports.input;

import br.com.helpdev.sample.core.domain.entities.User;

public interface UserCreatorPort {

   User createUser(User user);

}

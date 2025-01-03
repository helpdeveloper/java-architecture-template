package br.com.helpdev.sample.core.ports.input;

import java.util.UUID;

import br.com.helpdev.sample.core.domain.entities.User;

public interface UserGetterPort {

      User getUser(UUID uuid);

}

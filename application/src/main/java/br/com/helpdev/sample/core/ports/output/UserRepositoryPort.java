package br.com.helpdev.sample.core.ports.output;

import java.util.Optional;
import java.util.UUID;

import br.com.helpdev.sample.core.domain.entities.User;

public interface UserRepositoryPort {

   User save(User user);

   Optional<User> findByUuid(UUID uuid);
}

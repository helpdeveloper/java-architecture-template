package br.com.helpdev.sample.adapters.output.db;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import br.com.helpdev.sample.adapters.output.db.jpa.UserJpaRepository;
import br.com.helpdev.sample.adapters.output.db.mapper.UserDbMapper;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.ports.output.UserRepositoryPort;

@Repository
class UserRepository implements UserRepositoryPort {

   private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

   private final UserJpaRepository userJpaRepository;

   UserRepository(final UserJpaRepository userJpaRepository) {
      this.userJpaRepository = userJpaRepository;
   }

   @Override
   public User save(final User user) {
      final var userEntity = UserDbMapper.toEntity(user);
      final var userEntitySaved = userJpaRepository.save(userEntity);
      return user.withId(userEntitySaved.getId());
   }

   @Override
   public Optional<User> findByUuid(final UUID uuid) {
      final var userEntity = userJpaRepository.findByUuid(uuid.toString());
      logger.info("User found: {}", userEntity);
      return userEntity.map(UserDbMapper::toDomain);
   }
}

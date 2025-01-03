package br.com.helpdev.sample.adapters.output.db.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.helpdev.sample.adapters.output.db.jpa.entities.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

   Optional<UserEntity> findByUuid(String uuid);
}

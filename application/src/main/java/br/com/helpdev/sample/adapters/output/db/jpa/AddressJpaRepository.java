package br.com.helpdev.sample.adapters.output.db.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.helpdev.sample.adapters.output.db.jpa.entities.AddressEntity;

public interface AddressJpaRepository extends JpaRepository<AddressEntity, Long> {

}

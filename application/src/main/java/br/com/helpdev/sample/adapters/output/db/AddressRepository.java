package br.com.helpdev.sample.adapters.output.db;

import org.springframework.stereotype.Repository;

import br.com.helpdev.sample.adapters.output.db.jpa.AddressJpaRepository;
import br.com.helpdev.sample.adapters.output.db.jpa.entities.AddressEntity;
import br.com.helpdev.sample.adapters.output.db.mapper.AddressDbMapper;
import br.com.helpdev.sample.core.domain.entities.Address;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.ports.output.AddressRepositoryPort;

@Repository
class AddressRepository implements AddressRepositoryPort {

   private final AddressJpaRepository addressJpaRepository;

   AddressRepository(final AddressJpaRepository addressJpaRepository) {
      this.addressJpaRepository = addressJpaRepository;
   }

   @Override
   public Address save(final User user, final Address address) {
      AddressEntity savedAddress = addressJpaRepository.save(AddressDbMapper.toEntity(user.id(), address));
      return AddressDbMapper.toDomain(savedAddress);
   }
}

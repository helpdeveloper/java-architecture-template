package br.com.helpdev.sample.adapters.output.db.mapper;

import br.com.helpdev.sample.adapters.output.db.jpa.entities.AddressEntity;
import br.com.helpdev.sample.core.domain.entities.Address;

public class AddressDbMapper {

   private AddressDbMapper() {
   }

   public static Address toDomain(final AddressEntity addressEntity) {
      return Address.of(addressEntity.getId(), addressEntity.getCountry(), addressEntity.getState(), addressEntity.getCity(),
            addressEntity.getZipCode(), addressEntity.getStreetAddress());
   }

   public static AddressEntity toEntity(final Long userId, final Address address) {
      return new AddressEntity(address.id(), userId, address.country(), address.state(), address.city(), address.zipCode(), address.streetAddress());
   }
}

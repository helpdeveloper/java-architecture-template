package br.com.helpdev.sample.adapters.output.feign;

import org.springframework.stereotype.Service;

import br.com.helpdev.sample.adapters.output.feign.client.AddressFeignClient;
import br.com.helpdev.sample.core.domain.entities.Address;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.ports.output.AddressClientPort;

@Service
class AddressClient implements AddressClientPort {

   private final AddressFeignClient addressFeignClient;

   AddressClient(AddressFeignClient addressFeignClient) {
      this.addressFeignClient = addressFeignClient;
   }

   @Override
   public Address findUserAddress(final User user) {
      final var addressFromUser = addressFeignClient.generate();
      return Address.of(addressFromUser.country(), addressFromUser.state(), addressFromUser.city(), addressFromUser.zipCode(),
            addressFromUser.streetAddress());
   }
}

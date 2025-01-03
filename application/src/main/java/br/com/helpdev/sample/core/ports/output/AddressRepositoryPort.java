package br.com.helpdev.sample.core.ports.output;

import br.com.helpdev.sample.core.domain.entities.Address;
import br.com.helpdev.sample.core.domain.entities.User;

public interface AddressRepositoryPort {

   Address save(User user, Address address);

}

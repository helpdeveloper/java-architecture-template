package br.com.helpdev.sample.adapters.output.db;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.helpdev.sample.adapters.output.db.jpa.AddressJpaRepository;
import br.com.helpdev.sample.adapters.output.db.mapper.AddressDbMapper;
import br.com.helpdev.sample.core.domain.entities.Address;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.vo.Email;

@ExtendWith(MockitoExtension.class)
class AddressRepositoryTest {

   @Mock
   private AddressJpaRepository addressJpaRepository;

   @InjectMocks
   private AddressRepository addressRepository;

   private User user;

   private Address address;

   @BeforeEach
   void setUp() {
      user = User.of(1L, UUID.randomUUID(), "John Doe", Email.of("john.doe@example.com"), LocalDate.of(1990, 1, 1), null);
      address = Address.of("Country", "State", "City", "ZipCode", "StreetAddress");
   }

   @Test
   void testSave() {
      var addressEntity = AddressDbMapper.toEntity(user.id(), address);
      when(addressJpaRepository.save(any())).thenReturn(addressEntity);

      Address savedAddress = addressRepository.save(user, address);

      assertNotNull(savedAddress);
      assertEquals(address.country(), savedAddress.country());
      assertEquals(address.state(), savedAddress.state());
      assertEquals(address.city(), savedAddress.city());
      assertEquals(address.zipCode(), savedAddress.zipCode());
      assertEquals(address.streetAddress(), savedAddress.streetAddress());
      verify(addressJpaRepository).save(any());
   }
}
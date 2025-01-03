package br.com.helpdev.sample.adapters.output.feign;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.helpdev.sample.adapters.output.feign.client.AddressDto;
import br.com.helpdev.sample.adapters.output.feign.client.AddressFeignClient;
import br.com.helpdev.sample.core.domain.entities.Address;
import br.com.helpdev.sample.core.domain.entities.User;
import br.com.helpdev.sample.core.domain.vo.Email;

@ExtendWith(MockitoExtension.class)
class AddressClientTest {

   @Mock
   private AddressFeignClient addressFeignClient;

   @InjectMocks
   private AddressClient addressClient;

   private User user;

   @BeforeEach
   void setUp() {
      user = User.of("John Doe", Email.of("john.doe@example.com"), LocalDate.of(1990, 1, 1));
   }

   @Test
   void testFindUserAddress() {
      var addressFromUser = new AddressDto(1L, "", "City", "StreetAddress", "StreetAddress", "ZipCode", "StreetAddress" + "ss", "", "", "ZipCode", "",
            "", "", "", "", "", "State", "", "Country", "", 1.0, 1.0, "");
      when(addressFeignClient.generate()).thenReturn(addressFromUser);

      Address address = addressClient.findUserAddress(user);

      assertEquals("Country", address.country());
      assertEquals("State", address.state());
      assertEquals("City", address.city());
      assertEquals("ZipCode", address.zipCode());
      assertEquals("StreetAddress", address.streetAddress());
   }
}
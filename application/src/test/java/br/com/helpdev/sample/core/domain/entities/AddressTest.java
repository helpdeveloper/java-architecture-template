package br.com.helpdev.sample.core.domain.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressTest {

   @Test
   void shouldCreateAddress_whenAllFieldsAreValid() {
      assertDoesNotThrow(() -> new Address(1L, "Country", "State", "City", "12345", "Street Address"));
   }

   @Test
   void shouldThrowException_whenCountryIsNull() {
      assertThrows(IllegalArgumentException.class, () -> new Address(1L, null, "State", "City", "12345", "Street Address"));
   }

   @Test
   void shouldThrowException_whenStateIsNull() {
      assertThrows(IllegalArgumentException.class, () -> new Address(1L, "Country", null, "City", "12345", "Street Address"));
   }

   @Test
   void shouldThrowException_whenCityIsNull() {
      assertThrows(IllegalArgumentException.class, () -> new Address(1L, "Country", "State", null, "12345", "Street Address"));
   }

   @Test
   void shouldThrowException_whenZipCodeIsNull() {
      assertThrows(IllegalArgumentException.class, () -> new Address(1L, "Country", "State", "City", null, "Street Address"));
   }

   @Test
   void shouldThrowException_whenStreetAddressIsNull() {
      assertThrows(IllegalArgumentException.class, () -> new Address(1L, "Country", "State", "City", "12345", null));
   }

   @Test
   void shouldThrowException_whenCountryIsBlank() {
      assertThrows(IllegalArgumentException.class, () -> new Address(1L, "", "State", "City", "12345", "Street Address"));
   }

   @Test
   void shouldThrowException_whenStateIsBlank() {
      assertThrows(IllegalArgumentException.class, () -> new Address(1L, "Country", "", "City", "12345", "Street Address"));
   }

   @Test
   void shouldThrowException_whenCityIsBlank() {
      assertThrows(IllegalArgumentException.class, () -> new Address(1L, "Country", "State", "", "12345", "Street Address"));
   }

   @Test
   void shouldThrowException_whenZipCodeIsBlank() {
      assertThrows(IllegalArgumentException.class, () -> new Address(1L, "Country", "State", "City", "", "Street Address"));
   }

   @Test
   void shouldThrowException_whenStreetAddressIsBlank() {
      assertThrows(IllegalArgumentException.class, () -> new Address(1L, "Country", "State", "City", "12345", ""));
   }
}
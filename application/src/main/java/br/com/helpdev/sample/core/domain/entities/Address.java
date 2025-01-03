package br.com.helpdev.sample.core.domain.entities;

public record Address(Long id, String country, String state, String city, String zipCode, String streetAddress) {

   public Address {
      if (country == null || country.isBlank()) {
         throw new IllegalArgumentException("Country is required");
      }
      if (state == null || state.isBlank()) {
         throw new IllegalArgumentException("State is required");
      }
      if (city == null || city.isBlank()) {
         throw new IllegalArgumentException("City is required");
      }
      if (zipCode == null || zipCode.isBlank()) {
         throw new IllegalArgumentException("Zip code is required");
      }
      if (streetAddress == null || streetAddress.isBlank()) {
         throw new IllegalArgumentException("Street address is required");
      }
   }

   public static Address of(Long id, String country, String state, String city, String zipCode, String streetAddress) {
      return new Address(id, country, state, city, zipCode, streetAddress);
   }

   public static Address of(String country, String state, String city, String zipCode, String streetAddress) {
      return new Address(0L, country, state, city, zipCode, streetAddress);
   }

}

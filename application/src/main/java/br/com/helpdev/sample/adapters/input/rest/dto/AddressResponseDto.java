package br.com.helpdev.sample.adapters.input.rest.dto;

public record AddressResponseDto(
      String street,
      String city,
      String state,
      String country,
      String zipCode
) {

}

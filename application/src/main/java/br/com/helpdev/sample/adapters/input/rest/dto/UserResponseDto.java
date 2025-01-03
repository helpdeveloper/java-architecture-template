package br.com.helpdev.sample.adapters.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponseDto(
      String name,
      String email,
      @JsonProperty("birth_date") String birthDate,
      AddressResponseDto address
) {

}

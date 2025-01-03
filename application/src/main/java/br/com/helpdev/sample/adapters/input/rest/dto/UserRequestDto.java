package br.com.helpdev.sample.adapters.input.rest.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserRequestDto(
      @NotEmpty @NotNull String name,
      @NotEmpty @NotNull String email,
      @NotNull @JsonProperty("birth_date") LocalDate birthDate
){

}

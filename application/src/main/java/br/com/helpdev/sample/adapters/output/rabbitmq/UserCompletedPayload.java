package br.com.helpdev.sample.adapters.output.rabbitmq;

import static java.util.Objects.requireNonNull;

import io.swagger.v3.oas.annotations.media.Schema;

import br.com.helpdev.sample.core.domain.entities.Address;
import br.com.helpdev.sample.core.domain.entities.User;

record UserCompletedPayload(
      @Schema(title = "UUID", example = "uuid") String uuid,
      @Schema(title = "Name", example = "John Doe") String name,
      @Schema(title = "Email", example = "john.doe@example.com") String email,
      @Schema(title = "Birth date", example = "2000-01-01") String birthDate,
      @Schema(title = "Address") UserCompletedAddressPayload address) {

   static UserCompletedPayload of(final User user) {
      requireNonNull(user, "User is required");
      return new UserCompletedPayload(
            user.uuid().toString(),
            user.name(),
            user.email().value(),
            user.birthDate().toString(),
            UserCompletedAddressPayload.of(requireNonNull(user.address(), "Address is required for completed user payload")));
   }

}

record UserCompletedAddressPayload(
      @Schema(title = "Country", example = "Reunion") String country,
      @Schema(title = "State", example = "Arizona") String state,
      @Schema(title = "City", example = "East Nadia") String city,
      @Schema(title = "Zip code", example = "39781-7908") String zipCode,
      @Schema(title = "Street address", example = "849 Langosh Ports") String streetAddress) {

   static UserCompletedAddressPayload of(final Address address) {
      return new UserCompletedAddressPayload(
            address.country(),
            address.state(),
            address.city(),
            address.zipCode(),
            address.streetAddress());
   }

}

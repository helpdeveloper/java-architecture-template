package br.com.helpdev.sample.adapters.output.feign.client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AddressDto(Long id, String uid, String city, String streetName, String streetAddress, String secondaryAddress, String buildingNumber,
                         String mailBox, String community, String zipCode, String zip, String postcode, String timeZone, String streetSuffix,
                         String citySuffix, String cityPrefix, String state, String stateAbbr, String country, String countryCode, Double latitude,
                         Double longitude, String fullAddress) {

}

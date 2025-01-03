package br.com.helpdev.sample.adapters.input.kafka.dto;

public record UserEventDto(String event, String uuid) {

   public static final String EVENT_CREATED = "CREATED";

}

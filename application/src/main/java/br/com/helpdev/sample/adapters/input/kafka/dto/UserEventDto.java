package br.com.helpdev.sample.adapters.input.kafka.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserEventDto(
      @Schema(title = "Event", example = "CREATED|UPDATED") String event,
      @Schema(title = "UUID", example = "uuid") String uuid)
{

   public static final String EVENT_CREATED = "CREATED";

}

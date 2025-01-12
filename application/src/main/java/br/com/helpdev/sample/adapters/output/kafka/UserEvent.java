package br.com.helpdev.sample.adapters.output.kafka;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserEvent(
      @Schema(title = "Event", example = "CREATED|UPDATED") String event,
      @Schema(title = "UUID", example = "uuid") String uuid) {

   public static final String EVENT_CREATED = "CREATED";

   public static final String EVENT_UPDATED = "UPDATED";

   public static UserEvent ofCreated(UUID uuid) {
      return new UserEvent(EVENT_CREATED, uuid.toString());
   }

   public static UserEvent ofUpdated(UUID uuid) {
      return new UserEvent(EVENT_UPDATED, uuid.toString());
   }

   public String toJson() {
      return String.format("{\"event\":\"%s\",\"uuid\":\"%s\"}", event, uuid);
   }

}

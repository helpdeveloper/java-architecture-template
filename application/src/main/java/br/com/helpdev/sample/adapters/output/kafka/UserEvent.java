package br.com.helpdev.sample.adapters.output.kafka;

import java.util.UUID;

public record UserEvent(String event, String uuid) {

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

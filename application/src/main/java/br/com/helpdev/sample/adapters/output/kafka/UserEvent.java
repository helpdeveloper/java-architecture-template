package br.com.helpdev.sample.adapters.output.kafka;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserEvent(
      @Schema(title = "CloudEvent Spec Version", example = "1.0") String specversion,
      @Schema(title = "CloudEvent Identifier", example = "ef5f318c-4b7c-4fd7-a661-56293d8b8a91") String id,
      @Schema(title = "CloudEvent Source", example = "urn:helpdev:sample:user") String source,
      @Schema(title = "CloudEvent Type", example = "br.com.helpdev.sample.user.created") String type,
      @Schema(title = "CloudEvent Time", example = "2026-04-12T21:33:04Z") OffsetDateTime time,
      @Schema(title = "CloudEvent Data Content Type", example = "application/json") String datacontenttype,
      @Schema(title = "CloudEvent Data") UserEventData data)
{
}

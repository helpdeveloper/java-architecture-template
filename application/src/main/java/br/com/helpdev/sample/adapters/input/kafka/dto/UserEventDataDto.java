package br.com.helpdev.sample.adapters.input.kafka.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserEventDataDto(
      @Schema(title = "User UUID", example = "f0f8cf3e-e856-4d61-a613-44f5df7742ca") String userUuid)
{
}

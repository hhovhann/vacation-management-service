package com.hhovhann.vacation.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class ErrorResponseDto {
  @Schema(
      name = "message",
      example = "NOT_FOUND | BAD_REQUEST",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String message;

  @Schema(
      name = "details",
      example =
          "[There is no vacation request with id 2f8c7305-08e7-4ed5-a998-089ef1672c25] | [Bad Request]",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private List<String> details;
}

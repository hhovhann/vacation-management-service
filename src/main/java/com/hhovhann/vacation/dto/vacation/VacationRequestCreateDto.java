package com.hhovhann.vacation.dto.vacation;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacationRequestCreateDto {
  @JsonProperty("id")
  @Schema(
      name = "id",
      example = "2f8c7305-08e7-4ed5-a998-089ef1672c25",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  UUID entityId;

  @NotBlank(message = "Worker ID may not be null")
  @JsonProperty("author")
  @Schema(
      name = "author",
      example = "2f8c7305-08e7-4ed5-a998-089ef1672c25",
      requiredMode = Schema.RequiredMode.REQUIRED)
  UUID workerId;

  @NotBlank(message = "Manager ID by may not be null")
  @JsonProperty("resolved_by")
  @Schema(
      name = "resolved_by",
      example = "2f8c7305-08e7-4ed5-a998-089ef1672c25",
      requiredMode = Schema.RequiredMode.REQUIRED)
  UUID managerId;

  @Positive
  @NotBlank(message = "Requested vacation days may not be null")
  @JsonProperty("requested_days")
  @Schema(name = "requested_days", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
  Integer requestedVacationDays;

  @JsonProperty("request_created_at")
  @Schema(
      name = "request_created_at",
      example = "2024-08-09T12:57:13.506Z",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  ZonedDateTime requestCreatedAt;

  @NotBlank(message = "VacationRequest start date may not be null")
  @JsonProperty("vacation_start_date")
  @Schema(
      name = "vacation_start_date",
      example = "2024-08-01T12:57:13.506Z",
      requiredMode = Schema.RequiredMode.REQUIRED)
  ZonedDateTime vacationStartDate;

  @NotBlank(message = "VacationRequest end date may not be null")
  @JsonProperty("vacation_end_date")
  @Schema(
      name = "vacation_end_date",
      example = "2024-08-26T12:57:13.506Z",
      requiredMode = Schema.RequiredMode.REQUIRED)
  ZonedDateTime vacationEndDate;
}

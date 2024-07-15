package com.hhovhann.vacation.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hhovhann.vacation.enums.Title;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDto {
    @JsonProperty("id")
    @Schema(name = "Employee ID", example = "2f8c7305-08e7-4ed5-a998-089ef1672c25", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    UUID entityId;

    @NotBlank(message = "Employee full name may not be null")
    @JsonProperty("full_name")
    @Schema(name = "Employee full name", example = "Worker One | Manager One", requiredMode = Schema.RequiredMode.REQUIRED)
    String fullName;

    @Positive
    @NotNull(message = "Remaining Vacation Days by may not be null")
    @JsonProperty("remaining_vacation_days")
    @Schema(name = "Employee remaining vacation days", example = "15", defaultValue = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer remainingVacationDays;

    @NotNull(message = "Title may not be null")
    @JsonProperty("title")
    @Schema(name = "Employee title", example = "MANAGER | WORKER", requiredMode = Schema.RequiredMode.REQUIRED)
    Title title;
}

package com.hhovhann.vacation.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hhovhann.vacation.entity.VacationRequest;
import com.hhovhann.vacation.enums.Title;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class EmployeeResponseDto {
    @JsonProperty("id")
    @Schema(name = "Employee ID", example = "2f8c7305-08e7-4ed5-a998-089ef1672c25", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    UUID entityId;

    @JsonProperty("full_name")
    @Schema(name = "Employee full name", example = "Worker One | Manager One", requiredMode = Schema.RequiredMode.REQUIRED)
    String fullName;

    @JsonProperty("remaining_vacation_days")
    @Schema(name = "Employee remaining vacation days", example = "15", defaultValue = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer remainingVacationDays;

    @JsonProperty("title")
    @Schema(name = "Employee title", example = "MANAGER | WORKER", requiredMode = Schema.RequiredMode.REQUIRED)
    Title title;

    @JsonProperty("vacations")
    @Schema(name = "Employee list of vacation requests",
            example = """
                            [
                                {
                                    "id": "0e6f1c2c-d2bf-49da-9c98-1a0a5cb9a907",
                                    "author": "2f8c7305-08e7-4ed5-a998-089ef1672c25",
                                    "status": "PENDING",
                                    "resolved_by": "2923b9d2-89d5-4c5d-86ea-c33194f70835",
                                    "request_created_at": "2024-07-13T23:00:45.053836+04:00",
                                    "vacation_start_date": "2024-08-10T00:00:00Z",
                                    "vacation_end_date": "2024-08-25T00:00:00Z"
                                },
                                {
                                    "id": "871863b1-715c-4b68-bf9d-95e34bd39fc1",
                                    "author": "2f8c7305-08e7-4ed5-a998-089ef1672c25",
                                    "status": "PENDING",
                                    "resolved_by": "2923b9d2-89d5-4c5d-86ea-c33194f70835",
                                    "request_created_at": "2024-07-13T23:02:24.158446+04:00",
                                    "vacation_start_date": "2024-08-10T00:00:00Z",
                                    "vacation_end_date": "2024-08-25T00:00:00Z"
                                }
                            ]
                    """,
            defaultValue = "[]",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private List<VacationRequest> vacationRequests;
}

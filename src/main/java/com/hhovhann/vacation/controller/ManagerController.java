package com.hhovhann.vacation.controller;

import com.hhovhann.vacation.dto.vacation.VacationResponseDto;
import com.hhovhann.vacation.entity.Employee;
import com.hhovhann.vacation.entity.VacationRequest;
import com.hhovhann.vacation.mapper.VacationRequestMapper;
import com.hhovhann.vacation.service.employee.EmployeeService;
import com.hhovhann.vacation.service.vacation.VacationRequestService;
import com.hhovhann.vacation.enums.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "v1/api/manager")
@Tag(name = "Manager controller")
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ManagerController {
    EmployeeService employeeService;
    VacationRequestService vacationRequestService;
    VacationRequestMapper vacationRequestMapper;

    public ManagerController(EmployeeService employeeService, VacationRequestService vacationRequestService, VacationRequestMapper vacationRequestMapper) {
        this.employeeService = employeeService;
        this.vacationRequestService = vacationRequestService;
        this.vacationRequestMapper = vacationRequestMapper;
    }

    @Operation(summary = "Get vacation requests filtered by status", description = "See an overview of all vacation requests: Filter by pending and approved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found - The vacation request was not found")
    })
    @GetMapping("vacations/overview")
    ResponseEntity<List<VacationResponseDto>> getVacationRequestsByStatuses() {
        List<VacationRequest> vacationRequests = vacationRequestService.getAllVacationRequestsByStatus();

        List<VacationResponseDto> vacationResponseDtos = new ArrayList<>();
        vacationRequests.forEach(vacationRequest -> vacationResponseDtos.add(vacationRequestMapper.toResponseDto(vacationRequest)));

        return ResponseEntity.ok(vacationResponseDtos);
    }

    @Operation(summary = "Get an employee vacation requests", description = "See an overview for each individual employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found - The vacation request was not found")
    })
    @Parameter(name = "employeeId", description = "Employee id", example = "2f8c7305-08e7-4ed5-a998-089ef1672c25", required = true)
    @GetMapping("vacations/{employeeId}/overview")
    ResponseEntity<List<VacationResponseDto>> retrieveAllVacationRequestsByEmployeeIdAndStatus(@PathVariable(name = "employeeId") @NotNull UUID employeeId) {
        List<VacationRequest> vacationRequests = vacationRequestService.getAllVacationRequestsPerEmployee(employeeId);

        List<VacationResponseDto> vacationResponses = new ArrayList<>();
        vacationRequests.forEach(vacationRequest -> vacationResponses.add(vacationRequestMapper.toResponseDto(vacationRequest)));

        return ResponseEntity.ok(vacationResponses);

    }

    @Operation(summary = "Get overlapping vacation requests", description = "See an overview of overlapping requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found - The vacation request was not found")
    })
    @Parameter(name = "vacationStartDate", description = "Employee title", example = "2024-08-24T00:00:00.000Z", required = true)
    @Parameter(name = "vacationEndDate", description = "Employee title", example = "2024-09-04T00:00:00.000Z", required = true)
    @GetMapping("overlapping-vacations/overview")
    ResponseEntity<List<VacationResponseDto>> getOverlappingVacationRequests(@RequestParam(name = "vacation_start_date") @NotNull ZonedDateTime vacationStartDate,
                                                                             @RequestParam(name = "vacation_end_date") @NotNull ZonedDateTime vacationEndDate) {
        List<VacationRequest> allOverlappingVacationRequests = vacationRequestService.getAllOverlappingVacationRequests(vacationStartDate, vacationEndDate);// list of vacation requests with the same start dates

        List<VacationResponseDto> vacationResponses = new ArrayList<>();
        allOverlappingVacationRequests.forEach(vacationRequest -> vacationResponses.add(vacationRequestMapper.toResponseDto(vacationRequest)));

        return ResponseEntity.ok(vacationResponses);
    }

    @Operation(summary = "Process an employee vacation request", description = "Process an individual request and either approve or reject it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found - The vacation request was not found")
    })
    @Parameter(name = "vacationRequestId", description = "Vacation request id", example = "2f8c7305-08e7-4ed5-a998-089ef1672c25", required = true)
    @PostMapping("vacations/{vacationRequestId}")
    ResponseEntity<VacationResponseDto> processVacationRequests(@PathVariable @NotNull UUID vacationRequestId) {
        VacationRequest vacationRequest = vacationRequestService.getVacationRequest(vacationRequestId);

        boolean isOverlapping = vacationRequestService.isVacationRequestOverlapping(vacationRequest.getVacationStartDate(), vacationRequest.getVacationEndDate());
        if (isOverlapping) {
            log.warn("Vacation request dates overlap with an existing request.");
            vacationRequest.setStatus(Status.REJECTED);
        } else if (!vacationRequestService.checkSufficientEmployeePresence(vacationRequest.getVacationStartDate(), vacationRequest.getVacationEndDate())) {
            log.warn("Insufficient employees available during the requested period.");
            vacationRequest.setStatus(Status.REJECTED);
        } else { // check how many employee in office
            Employee employee = vacationRequest.getEmployee();
            employee = employeeService.deductVacationDaysFromEmployee(employee, vacationRequest.getRequestedDays());
            employeeService.saveAllEmployees(List.of(employee));

            vacationRequest.setEmployee(employee);
            vacationRequest.setStatus(Status.APPROVED);
        }
        VacationRequest storedVacationRequest = vacationRequestService.storeVacationRequest(vacationRequest);

        return ResponseEntity.ok(vacationRequestMapper.toResponseDto(storedVacationRequest));
    }
}

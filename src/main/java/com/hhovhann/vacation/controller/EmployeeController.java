package com.hhovhann.vacation.controller;

import com.hhovhann.vacation.dto.employee.EmployeeCreateDto;
import com.hhovhann.vacation.dto.employee.EmployeeResponseDto;
import com.hhovhann.vacation.dto.model.ErrorResponseDto;
import com.hhovhann.vacation.dto.vacation.VacationRequestCreateDto;
import com.hhovhann.vacation.dto.vacation.VacationResponseDto;
import com.hhovhann.vacation.entity.Employee;
import com.hhovhann.vacation.entity.VacationRequest;
import com.hhovhann.vacation.exception.VacationValidationException;
import com.hhovhann.vacation.mapper.EmployeeMapper;
import com.hhovhann.vacation.mapper.VacationRequestMapper;
import com.hhovhann.vacation.service.employee.EmployeeService;
import com.hhovhann.vacation.service.vacation.VacationRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("v1/api/employees")
@Tag(name = "Employee controller")
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class EmployeeController {
  EmployeeService employeeService;
  EmployeeMapper employeeMapper;
  VacationRequestService vacationRequestService;
  VacationRequestMapper vacationRequestMapper;

  public EmployeeController(
      EmployeeService employeeService,
      EmployeeMapper employeeMapper,
      VacationRequestService vacationRequestService,
      VacationRequestMapper vacationRequestMapper) {
    this.employeeService = employeeService;
    this.employeeMapper = employeeMapper;
    this.vacationRequestService = vacationRequestService;
    this.vacationRequestMapper = vacationRequestMapper;
  }

  @Operation(
      summary = "Get an employee vacation requests by status",
      description =
          "Retrieves employee vacation requests: Filter by status (APPROVED, PENDING, REJECTED)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = VacationResponseDto.class))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponseDto.class))
            })
      })
  @GetMapping("{employeeId}/vacations")
  ResponseEntity<List<VacationResponseDto>> getVacationRequests(
      @PathVariable
          @NotNull
          @Parameter(
              name = "employeeId",
              description = "Employee id",
              example = "2f8c7305-08e7-4ed5-a998-089ef1672c25",
              required = true)
          UUID employeeId) {
    List<VacationRequest> vacationRequests =
        vacationRequestService.getEmployeeVacationRequestsForAllStatuses(employeeId);

    List<VacationResponseDto> vacationResponses = new ArrayList<>();
    vacationRequests.forEach(
        vacationRequest ->
            vacationResponses.add(vacationRequestMapper.toResponseDto(vacationRequest)));

    return ResponseEntity.ok(vacationResponses);
  }

  @Operation(
      summary = "Get an employee number of remaining vacation days",
      description = "Retrieves employee number of remaining vacation days")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponseDto.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Not found - The employee was not found",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponseDto.class))
            })
      })
  @GetMapping("{employeeId}/remaining-vacations")
  ResponseEntity<Integer> getEmployeeRemainingVacationDays(
      @PathVariable
          @NotNull
          @Parameter(
              name = "employeeId",
              description = "Employee id",
              example = "2f8c7305-08e7-4ed5-a998-089ef1672c25",
              required = true)
          UUID employeeId) {
    int remainingVacationDays = employeeService.findEmployeeRemainingVacationDays(employeeId);

    return ResponseEntity.ok(remainingVacationDays);
  }

  @Operation(summary = "Creates a list of employees", description = "Creates a list of employees")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully created"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponseDto.class))
            })
      })
  @PostMapping
  ResponseEntity<List<EmployeeResponseDto>> createEmployees(
      @Valid @RequestBody List<EmployeeCreateDto> employeeCreateDtos) {
    List<Employee> employees = new ArrayList<>();
    employeeCreateDtos.forEach(
        employeeCreateDto -> employees.add(employeeMapper.toEmployee(employeeCreateDto)));

    List<Employee> storedEmployees = employeeService.saveAllEmployees(employees);

    List<EmployeeResponseDto> employeeResponses = new ArrayList<>();
    storedEmployees.forEach(
        savedEmployee -> employeeResponses.add(employeeMapper.toResponseDto(savedEmployee)));

    return ResponseEntity.ok(employeeResponses);
  }

  @Operation(
      summary = "Creates a new vacation request",
      description =
          "Creates a new vacation request: If they have not exhausted their total limit (30 per year)")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully created"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponseDto.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Not found - The employee was not found",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponseDto.class))
            })
      })
  @PostMapping("vacation-request")
  ResponseEntity<VacationResponseDto> createVacationRequest(
      @NotNull @RequestBody VacationRequestCreateDto vacationRequestCreateDto) {
    int remainingVacationDays =
        employeeService.findEmployeeRemainingVacationDays(vacationRequestCreateDto.getWorkerId());
    if (remainingVacationDays <= 0
        || vacationRequestCreateDto.getRequestedVacationDays() > remainingVacationDays) {
      throw new VacationValidationException(
          " There is not enough days in your remaining vacation days balance.");
    }

    VacationRequest vacationRequest =
        vacationRequestService.createVacationRequest(vacationRequestCreateDto);

    return ResponseEntity.ok(vacationRequestMapper.toResponseDto(vacationRequest));
  }

  @Operation(
      summary = "Creates a new list of vacation requests",
      description =
          "Creates a new list of vacation requests: If they have not exhausted their total limit (30 per year)")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully created"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponseDto.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Not found - The employee was not found",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ErrorResponseDto.class))
            })
      })
  @PostMapping("vacation-requests")
  ResponseEntity<List<VacationResponseDto>> createVacationRequests(
      @RequestBody List<VacationRequestCreateDto> vacationRequestCreateDtos) {
    List<VacationResponseDto> responseBody = new ArrayList<>();
    vacationRequestCreateDtos.forEach(
        vacationRequestCreateDto -> {
          int remainingVacationDays =
              employeeService.findEmployeeRemainingVacationDays(
                  vacationRequestCreateDto.getWorkerId());
          if (remainingVacationDays <= 0
              || vacationRequestCreateDto.getRequestedVacationDays() > remainingVacationDays) {
            throw new VacationValidationException(
                " There is not enough days in your remaining vacation days balance.");
          }
        });

    List<VacationRequest> vacationRequests =
        vacationRequestService.createVacationRequests(vacationRequestCreateDtos);
    vacationRequests.forEach(
        vacationRequest -> responseBody.add(vacationRequestMapper.toResponseDto(vacationRequest)));

    return ResponseEntity.ok(responseBody);
  }
}

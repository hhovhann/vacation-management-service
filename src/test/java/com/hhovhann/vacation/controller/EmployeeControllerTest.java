package com.hhovhann.vacation.controller;

import com.hhovhann.AbstractIntegrationTest;
import com.hhovhann.vacation.dto.employee.EmployeeCreateDto;
import com.hhovhann.vacation.dto.employee.EmployeeResponseDto;
import com.hhovhann.vacation.dto.vacation.VacationRequestCreateDto;
import com.hhovhann.vacation.dto.vacation.VacationResponseDto;
import com.hhovhann.vacation.enums.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

class EmployeeControllerTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "v1/api/employees";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should create the employees")
    void shouldCreateEmployees() {
        // given
        var requestUrl = "http://localhost:" + port + "/" + BASE_URL;
        var requestBody = EmployeeCreateDto.builder()
                .fullName("Manager One")
                .remainingVacationDays(30)
                .title(Title.MANAGER).build();

        // when
        var responseEntity = this.restTemplate.postForEntity(requestUrl, List.of(requestBody), EmployeeResponseDto[].class);

        // then
        assertAll(
                "Grouped Assertions of Feeds",
                () -> assertTrue(responseEntity.hasBody(), "Response should has a body")
        );
    }


    @Test
    @DisplayName("Should create the employees and vacation requests")
    void shouldCreateEmployeesVacationRequests() {
        // given
        var requestUrl = "http://localhost:" + port + "/" + BASE_URL;

        var managerOne = EmployeeCreateDto.builder()
                .fullName("Manager One")
                .remainingVacationDays(30)
                .title(Title.MANAGER).build();

        var workerOne = EmployeeCreateDto.builder()
                .fullName("Worker One")
                .remainingVacationDays(30)
                .title(Title.WORKER).build();

        var workerTwo = EmployeeCreateDto.builder()
                .fullName("Worker Two")
                .remainingVacationDays(30)
                .title(Title.WORKER).build();

        // when
        ResponseEntity<EmployeeResponseDto[]> employeesResponseEntity = this.restTemplate.postForEntity(requestUrl, List.of(managerOne, workerOne, workerTwo), EmployeeResponseDto[].class);

        // then
        assertAll(
                "Grouped Assertions of Feeds",
                () -> assertTrue(employeesResponseEntity.hasBody(), "Response should has a body")
        );

        requestUrl = "http://localhost:" + port + "/" + BASE_URL + "/" + "vacation-requests";

        EmployeeResponseDto managerOneResponse = Objects.requireNonNull(employeesResponseEntity.getBody())[0];
        EmployeeResponseDto workerOneResponse = Objects.requireNonNull(employeesResponseEntity.getBody())[1];
        EmployeeResponseDto workerTwoResponse = Objects.requireNonNull(employeesResponseEntity.getBody())[2];

        VacationRequestCreateDto vacationRequestOne = VacationRequestCreateDto.builder()
                .managerId(managerOneResponse.getEntityId())
                .workerId(workerOneResponse.getEntityId())
                .requestedVacationDays(10)
                .vacationStartDate(ZonedDateTime.now().plusMonths(1))
                .vacationEndDate(ZonedDateTime.now().plusMonths(1).plusDays(10)).build();

        var vacationRequestResponseEntity = this.restTemplate.postForEntity(requestUrl, List.of(vacationRequestOne), VacationResponseDto[].class);

        // then
        assertAll(
                "Grouped Assertions of Feeds",
                () -> assertTrue(vacationRequestResponseEntity.hasBody(), "Response should has a body")
        );

        VacationRequestCreateDto vacationRequestTwo = VacationRequestCreateDto.builder()
                .managerId(managerOneResponse.getEntityId())
                .workerId(workerTwoResponse.getEntityId())
                .requestedVacationDays(10)
                .vacationStartDate(ZonedDateTime.now().plusMonths(1))
                .vacationEndDate(ZonedDateTime.now().plusMonths(1).plusDays(10)).build();

        var vacationRequestTwoResponseEntity = this.restTemplate.postForEntity(requestUrl, List.of(vacationRequestTwo), VacationResponseDto[].class);
        // then
        assertAll(
                "Grouped Assertions of Feeds",
                () -> assertTrue(vacationRequestTwoResponseEntity.hasBody(), "Response should has a body")
        );
    }
}
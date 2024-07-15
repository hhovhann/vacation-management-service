package com.hhovhann.vacation.service.vacation;

import com.hhovhann.vacation.dto.vacation.VacationRequestCreateDto;
import com.hhovhann.vacation.entity.VacationRequest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface VacationRequestService {
    /**
     * Retrieves vacation request by vacationRequestId
     * @param vacationRequestId vacation request instance UUID
     * @return list of vacation requests for all employees
     */
    VacationRequest getVacationRequest(UUID vacationRequestId);

    /**
     * Retrieves employee all vacation requests for all statuses: APPROVED, REJECTED, PENDING
     * @param employeeId employee instance id
     * @return list of existing vacations, otherwise throw VacationRequestNotFoundException
     */
    List<VacationRequest> getEmployeeVacationRequestsForAllStatuses(UUID employeeId);


    /**
     * Retrieves all vacation requests by statuses: PENDING, APPROVAL
     * @return list of vacation requests for all employees
     */
    List<VacationRequest> getAllVacationRequestsByStatus();

    /**
     * Retrieves all vacation requests per employee
     * @return list of vacation requests for all employees
     */
    List<VacationRequest> getAllVacationRequestsPerEmployee(UUID employeeId);

    /**
     * Retrieves al  overlapping requests for provided period
     * @param vacationStartDate vacation request start date
     * @param vacationEndDate vacation request end date
     * @return list of vacation requests for all employees
     */
    List<VacationRequest> getAllOverlappingVacationRequests(ZonedDateTime vacationStartDate, ZonedDateTime vacationEndDate) ;

    /**
     * Retrieves all overlapping APPROVED requests for provided period
     * @param vacationStartDate vacation request start date
     * @param vacationEndDate vacation request end date
     * @return list of vacation requests for all employees
     */
    List<VacationRequest> getAllOverlappingApprovedVacationRequests(ZonedDateTime vacationStartDate, ZonedDateTime vacationEndDate) ;


    /**
     * Save into storage the vacation request instance
     * @param vacationRequest vacation request instance
     * @return stored vacation request instance
     */
    VacationRequest storeVacationRequest(VacationRequest vacationRequest);

    /**
     * Creates vacation request
     * @param vacationRequestCreateDto vacation request request dto instance
     * @return vacation Request instance
     */
    VacationRequest createVacationRequest(VacationRequestCreateDto vacationRequestCreateDto);

    /**
     * Creates list of vacation requests
     * @param vacationRequestCreateDtos list of vacation requests dto instance
     * @return vacation Request instance
     */
    List<VacationRequest> createVacationRequests(List<VacationRequestCreateDto> vacationRequestCreateDtos);

    /**
     * Checks is request overlapping with APPROVED other requests
     * @param vacationStartDate vacation request start date
     * @param vacationEndDate vacation request end date
     * @return return true if request is overlapping, otherwise false
     */
    boolean isVacationRequestOverlapping(ZonedDateTime vacationStartDate, ZonedDateTime vacationEndDate);

    /**
     * Checks is sufficient employee presence
     * @param vacationStartDate vacation request start date
     * @param vacationEndDate vacation request end date
     * @return return true if sufficient, otherwise false
     */
    boolean checkSufficientEmployeePresence(ZonedDateTime vacationStartDate, ZonedDateTime vacationEndDate);
}

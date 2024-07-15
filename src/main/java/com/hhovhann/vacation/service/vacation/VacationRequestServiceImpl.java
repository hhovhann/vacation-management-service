package com.hhovhann.vacation.service.vacation;

import com.hhovhann.vacation.dto.vacation.VacationRequestCreateDto;
import com.hhovhann.vacation.entity.Employee;
import com.hhovhann.vacation.entity.VacationRequest;
import com.hhovhann.vacation.enums.Status;
import com.hhovhann.vacation.exception.VacationRequestNotFoundException;
import com.hhovhann.vacation.mapper.VacationRequestMapper;
import com.hhovhann.vacation.repository.VacationRequestRepository;
import com.hhovhann.vacation.service.employee.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class VacationRequestServiceImpl implements VacationRequestService {
    EmployeeService employeeService;
    VacationRequestMapper vacationRequestMapper;
    VacationRequestRepository vacationRequestRepository;

    private static final int MINIMUM_EMPLOYEES_REQUIRED = 5; // Example value

    @Override
    public VacationRequest getVacationRequest(UUID vacationRequestId) {
        return vacationRequestRepository.findById(vacationRequestId).orElseThrow(() -> new VacationRequestNotFoundException("There is no vacation request with id " + vacationRequestId));
    }

    @Override
    public List<VacationRequest> getEmployeeVacationRequestsForAllStatuses(UUID employeeId) {
        return vacationRequestRepository.findByEmployeeIdAndStatusIn(employeeId, List.of(Status.PENDING, Status.APPROVED, Status.REJECTED)).orElseThrow(() -> new VacationRequestNotFoundException("There is no vacation request with employee id " + employeeId));
    }


    @Override
    public List<VacationRequest> getAllVacationRequestsByStatus() {
        return vacationRequestRepository.findByStatusIn(List.of(Status.PENDING, Status.APPROVED)).orElseThrow(() -> new VacationRequestNotFoundException("There is no vacation request with statuses PENDING or APPROVED."));
    }

    @Override
    public List<VacationRequest> getAllVacationRequestsPerEmployee(UUID employeeId) {
        return vacationRequestRepository.findByEmployeeId(employeeId).orElseThrow(() -> new VacationRequestNotFoundException("There is no vacation request with employee id " + employeeId));
    }

    @Override
    public List<VacationRequest> getAllOverlappingVacationRequests(ZonedDateTime vacationStartDate, ZonedDateTime vacationEndDate) {
        return vacationRequestRepository.findOverlappingRequests(vacationStartDate, vacationEndDate).orElseThrow(() -> new VacationRequestNotFoundException("There is no vacation request for provided period with startDate " + vacationStartDate + " and endDate " + vacationEndDate));
    }

    @Override
    public List<VacationRequest> getAllOverlappingApprovedVacationRequests(ZonedDateTime vacationStartDate, ZonedDateTime vacationEndDate) {
        return vacationRequestRepository.findOverlappingApprovedRequests(vacationStartDate, vacationEndDate).orElseThrow(VacationRequestNotFoundException::new);
    }

    @Override
    public boolean
    isVacationRequestOverlapping(ZonedDateTime vacationStartDate, ZonedDateTime vacationEndDate) {
        List<VacationRequest> overlappingRequests = getAllOverlappingApprovedVacationRequests(vacationStartDate, vacationEndDate);
        return !overlappingRequests.isEmpty();

    }

    @Override
    public boolean checkSufficientEmployeePresence(ZonedDateTime vacationStartDate, ZonedDateTime vacationEndDate) {
        long approvedRequestsOnDate = vacationRequestRepository.countApprovedVacationRequestsOnDate(vacationStartDate, vacationEndDate);
        return approvedRequestsOnDate < MINIMUM_EMPLOYEES_REQUIRED;
    }

    @Override
    public VacationRequest storeVacationRequest(VacationRequest vacationRequest) {
        return vacationRequestRepository.save(vacationRequest);
    }


    @Override
    public VacationRequest createVacationRequest(VacationRequestCreateDto vacationRequestCreateDto) {
        Employee employee = employeeService.findEmployeeById(vacationRequestCreateDto.getWorkerId());

        // save new vacation request and store into database (in future can support update to modify created vac)
        VacationRequest vacationRequest = vacationRequestMapper.toVacationRequest(vacationRequestCreateDto, employee);

        return vacationRequestRepository.save(vacationRequest);

    }

    @Override
    public List<VacationRequest> createVacationRequests(List<VacationRequestCreateDto> vacationRequestCreateDtos) {
        List<VacationRequest> vacationRequests = new ArrayList<>();

        // Collect all employee ids from the dto object
        List<UUID> employeeIds = vacationRequestCreateDtos.stream().map(VacationRequestCreateDto::getWorkerId).toList();

        // Find all employees where dto id's in list
        List<Employee> employees = employeeService.findEmployeesByIds(employeeIds);

        vacationRequestCreateDtos.forEach(vacationRequestCreateDto -> {
            // Get the employee by worker id, otherwise throws an exception
            Employee employee = employeeService.getEmployeeById(employees, vacationRequestCreateDto.getWorkerId()); // 2a415693-5b55-4e35-967c-d8a8bfb1aec9

            // save new vacation request and store into database (in future can support update to modify created vac)
            VacationRequest vacationRequest = vacationRequestMapper.toVacationRequest(vacationRequestCreateDto, employee);

            vacationRequests.add(vacationRequest);
        });

        return vacationRequestRepository.saveAll(vacationRequests);

    }
}

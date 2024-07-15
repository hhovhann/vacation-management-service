package com.hhovhann.vacation.mapper;

import com.hhovhann.vacation.dto.vacation.VacationRequestCreateDto;
import com.hhovhann.vacation.dto.vacation.VacationResponseDto;
import com.hhovhann.vacation.entity.Employee;
import com.hhovhann.vacation.entity.VacationRequest;
import com.hhovhann.vacation.enums.Status;
import org.springframework.stereotype.Component;

@Component
public class VacationRequestMapper {
    public VacationRequest toVacationRequest(VacationRequestCreateDto vacationRequestCreateDto, Employee employee) {
        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.setEmployee(employee);
        vacationRequest.setAuthor(vacationRequestCreateDto.getWorkerId());
        vacationRequest.setStatus(Status.PENDING);
        vacationRequest.setRequestedDays(vacationRequestCreateDto.getRequestedVacationDays());
        vacationRequest.setResolvedBy(vacationRequestCreateDto.getManagerId());
        vacationRequest.setVacationStartDate(vacationRequestCreateDto.getVacationStartDate());
        vacationRequest.setVacationEndDate(vacationRequestCreateDto.getVacationEndDate());

        return vacationRequest;
    }

    public VacationResponseDto toResponseDto(VacationRequest vacationRequest) {

        return VacationResponseDto.builder()
                .entityId(vacationRequest.getId())
                .workerId(vacationRequest.getAuthor())
                .status(vacationRequest.getStatus())
                .managerId(vacationRequest.getResolvedBy())
                .requestedVacationDays(vacationRequest.getRequestedDays())
                .requestCreatedAt(vacationRequest.getRequestCreatedAt())
                .vacationStartDate(vacationRequest.getVacationStartDate())
                .vacationEndDate(vacationRequest.getVacationEndDate())
                .build();
    }
}

package com.hhovhann.vacation.mapper;

import com.hhovhann.vacation.dto.employee.EmployeeCreateDto;
import com.hhovhann.vacation.dto.employee.EmployeeResponseDto;
import com.hhovhann.vacation.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEmployee(EmployeeCreateDto employeeCreateDto) {
        Employee employee = new Employee();
        employee.setFullName(employeeCreateDto.getFullName());
        employee.setTitle(employeeCreateDto.getTitle());
        employee.setRemainingVacationDays(employeeCreateDto.getRemainingVacationDays());

        return employee;
    }

    public EmployeeResponseDto toResponseDto(Employee employee) {
        return EmployeeResponseDto.builder()
                .entityId(employee.getId())
                .title(employee.getTitle())
                .fullName(employee.getFullName())
                .remainingVacationDays(employee.getRemainingVacationDays())
                .vacationRequests(employee.getVacationRequests())
                .build();
    }
}

package com.hhovhann.vacation.service.employee;

import com.hhovhann.vacation.entity.Employee;
import com.hhovhann.vacation.exception.EmployeeNotFoundException;
import com.hhovhann.vacation.exception.EmployeeValidationException;
import com.hhovhann.vacation.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class EmployeeServiceImpl implements EmployeeService {
    EmployeeRepository employeeRepository;

    @Override
    public int findEmployeeRemainingVacationDays(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);

        return employee.getRemainingVacationDays();
    }

    public Employee findEmployeeById(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("There is no employee with id " + employeeId));
    }

    public List<Employee> findEmployeesByIds(List<UUID> employeeIds) {
        return employeeRepository.findByIdIsIn(employeeIds);
    }

    public Employee deductVacationDaysFromEmployee(Employee employee, int requestedDays) {
        int remainingVacationDays = employee.getRemainingVacationDays() - requestedDays;
        if (remainingVacationDays <= 0) {
            throw new EmployeeValidationException("There is no enough vacation days. Please check your vacation balance.");
        }
        employee.setRemainingVacationDays(remainingVacationDays);

        return employee;
    }

    @Override
    public List<Employee> saveAllEmployees(List<Employee> employees) {
        return employeeRepository.saveAll(employees);
    }

    @Override
    public Employee getEmployeeById(List<Employee> employees, UUID employeeId) {
        return employees.stream()
                .filter(employee -> employeeId.equals(employee.getId()))
                .findFirst()
                .orElseThrow(() -> new EmployeeNotFoundException("There is no employee with id " + employeeId));
    }
}

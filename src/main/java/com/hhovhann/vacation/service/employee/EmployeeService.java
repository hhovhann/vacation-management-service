package com.hhovhann.vacation.service.employee;

import com.hhovhann.vacation.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    /**
     * Checks how many remaining vacation days employee has
     * @param employeeId employee instance id
     * @return the amount of remaining vacation days of an employee
     */
    int findEmployeeRemainingVacationDays(UUID employeeId);

    /**
     * Find employee by id
     *  @param employeeId employee instance id
     *  @return the employee instance
     */
    Employee findEmployeeById(UUID employeeId);

    /**
     * Find employees by ids
     *  @param employeeIds employee instance list of ids
     *  @return the employee instance
     */
    List<Employee> findEmployeesByIds(List<UUID> employeeIds);


    /**
     * Deducts requested days from the employee remaining days
     * @param employee employee  remaining vacation days
     * @param requestedDays requested vacation days by employee
     * @return employee instance with updated remaining vacation days
     */
    Employee deductVacationDaysFromEmployee(Employee employee, int requestedDays);

    /**
     * Save all employees into data storage
     * @param employees list of employee instances
     * @return stored list of employees
     */
    List<Employee> saveAllEmployees(List<Employee> employees);

    /**
     * Get the employee by employee id, if it's in list, otherwise throws EmployeeNotFound Exception
     * @param employees – list of employee instances
     * @param employeeId – list of employee instances
     * @return employee instance
     */
    Employee getEmployeeById(List<Employee> employees, UUID employeeId);
}

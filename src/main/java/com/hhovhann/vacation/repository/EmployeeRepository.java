package com.hhovhann.vacation.repository;

import com.hhovhann.vacation.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByIdIsIn(List<UUID> employeeIds);
}

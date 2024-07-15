package com.hhovhann.vacation.repository;

import com.hhovhann.vacation.entity.VacationRequest;
import com.hhovhann.vacation.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, UUID> {
    Optional<List<VacationRequest>> findByEmployeeId(UUID employeeId);

    Optional<List<VacationRequest>> findByEmployeeIdAndStatusIn(UUID employeeId, List<Status> statuses);

    Optional<List<VacationRequest>> findByStatusIn(List<Status> statuses);

    @Query("SELECT vr FROM VacationRequest vr WHERE (vr.vacationStartDate <= :vacationEndDate AND vr.vacationEndDate >= :vacationStartDate)")
    Optional<List<VacationRequest>> findOverlappingRequests(@Param("vacationStartDate") ZonedDateTime vacationStartDate, @Param("vacationEndDate") ZonedDateTime vacationEndDate);

    @Query("SELECT vr FROM VacationRequest vr WHERE (vr.vacationStartDate <= :vacationEndDate AND vr.vacationEndDate >= :vacationStartDate) AND vr.status = 'APPROVED'")
    Optional<List<VacationRequest>> findOverlappingApprovedRequests(@Param("vacationStartDate") ZonedDateTime vacationStartDate, @Param("vacationEndDate") ZonedDateTime vacationEndDate);

    @Query("SELECT COUNT(vr) FROM VacationRequest vr WHERE vr.vacationStartDate <= :vacationEndDate AND vr.vacationEndDate >= :vacationStartDate AND vr.status = 'APPROVED'")
    long countApprovedVacationRequestsOnDate(@Param("vacationStartDate") ZonedDateTime vacationStartDate, @Param("vacationEndDate") ZonedDateTime vacationEndDate);
}

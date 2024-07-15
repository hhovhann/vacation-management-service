package com.hhovhann.vacation.entity;

import com.hhovhann.vacation.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VacationRequest {

    @Id
    @UuidGenerator
    private UUID id; // ENTITY_ID

    @ManyToOne
    @JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "fk_vacation_request_employee_id"))
    private Employee employee;

    @Column(name = "worker_id")
    private UUID author;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "manager_id")
    private UUID resolvedBy;

    @Column(name = "requested_days")
    private Integer requestedDays;

    @Column(name = "request_created_at")
    private ZonedDateTime requestCreatedAt;

    @Column(name = "vacation_start_date")
    private ZonedDateTime vacationStartDate;

    @Column(name = "vacation_end_date")
    private ZonedDateTime vacationEndDate;

    @PrePersist
    void preInsert() {
        if (this.requestCreatedAt == null) {
            this.requestCreatedAt = ZonedDateTime.now();
        }
    }
}

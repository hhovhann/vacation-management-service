package com.hhovhann.vacation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhovhann.vacation.enums.Title;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id
    @UuidGenerator
    private UUID id; // ENTITY_ID

    @NotBlank
    @Column(name = "full_name")
    private String fullName; // name Lastname

    @NotNull
    @Positive
    @Column(name = "remaining_vacation_days", columnDefinition = "int default 30")
    private Integer remainingVacationDays;  // defaults are 30

    @NotNull
    @Enumerated(STRING)
    private Title title; // MANAGER or WORKER

    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<VacationRequest> vacationRequests = new ArrayList<>();
}

package ru.progressnw.employees.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Необходимо название отдела")
    private String name;

    @NotNull(message = "Необходимо задать начальника отдела")
    private long managerId;
}

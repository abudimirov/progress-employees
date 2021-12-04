package ru.progressnw.employees.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Имя обязательно для заполнения")
    private String firstname;
    @NotBlank(message = "Фамилия обязательно для заполнения")
    private String lastname;
    private String middlename;
    private String jobTitle;

    @NotBlank(message = "Логин обязателен для заполнения")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Пароль обязателен для заполнения")
    private String password;
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}

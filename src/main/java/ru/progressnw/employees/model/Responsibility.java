package ru.progressnw.employees.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
@Table(name = "responsibilities")
public class Responsibility {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "deputy_user_id")
    private User deputy;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "from_user_id")
    private List<User> fromUser;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "to_user_id")
    private List<User> toUser;

    private String link;

    @NotBlank(message = "Необходимо добавить описание")
    private String description;

    private boolean isBlocked;
}

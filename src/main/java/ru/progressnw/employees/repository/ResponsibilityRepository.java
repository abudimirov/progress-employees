package ru.progressnw.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;

import java.util.List;

@Repository
public interface ResponsibilityRepository extends JpaRepository<Responsibility, Long> {
    List<Responsibility> findAllByOrderByDescription();
    List<Responsibility> findByUser(User user);
    List<Responsibility> findByDeputy(User user);
}


package ru.progressnw.employees.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;

import java.util.List;

@Repository
public interface ResponsibilityRepository extends CrudRepository<Responsibility, Long> {
    List<Responsibility> findByUser(User user);
}
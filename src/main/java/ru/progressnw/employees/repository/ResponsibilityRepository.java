package ru.progressnw.employees.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.progressnw.employees.model.Responsibility;

@Repository
public interface ResponsibilityRepository extends CrudRepository<Responsibility, Long> {}
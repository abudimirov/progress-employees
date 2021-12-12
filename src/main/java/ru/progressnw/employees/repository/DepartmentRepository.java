package ru.progressnw.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.progressnw.employees.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}

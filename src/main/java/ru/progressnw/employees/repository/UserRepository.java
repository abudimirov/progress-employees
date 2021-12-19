package ru.progressnw.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.progressnw.employees.model.Department;
import ru.progressnw.employees.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByOrderByLastname();
    User findByUsername(String username);
    List<User> findAllByDepartment(Department department);
}
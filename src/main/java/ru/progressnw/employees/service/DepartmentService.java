package ru.progressnw.employees.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.progressnw.employees.model.Department;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.DepartmentRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public boolean userIsAnotherDepartmentAdmin(User user, Department department) {
        boolean isAnotherAdmin = false;
        List<Department> departmentList = departmentRepository.findAll();
        for (Department d : departmentList) {
            if (d.getId() != department.getId()) {
                isAnotherAdmin = d.getManagerId() == user.getId();
            }
        }
        log.info("User {} is manager for department {} and several more departments",
            user.getFirstname() + " " + user.getLastname(),
            department.getName());
        return isAnotherAdmin;
    }

    public List<Department> getDepartmentListByManager(User manager) {
        List<Department> departments = new ArrayList<>(Collections.emptyList());
        for (Department department : departmentRepository.findAll()) {
            if (department.getManagerId() == manager.getId()) {
                departments.add(department);
            }
        }
        departments.sort(Comparator.comparing(Department::getName, Comparator.comparing(s -> s.toLowerCase(Locale.ROOT))));
        return departments;
    }
}

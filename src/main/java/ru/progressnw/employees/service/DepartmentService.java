package ru.progressnw.employees.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.progressnw.employees.model.Department;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.DepartmentRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        return isAnotherAdmin;
    }

    public List<Department> getDepartmentListByManager(User manager) {
        List<Department> departments = new ArrayList<>(Collections.emptyList());
        for (Department department : departmentRepository.findAll()) {
            if (department.getManagerId() == manager.getId()) {
                departments.add(department);
            }
        }
        return departments;
    }
}

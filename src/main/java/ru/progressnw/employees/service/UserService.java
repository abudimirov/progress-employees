package ru.progressnw.employees.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.progressnw.employees.model.Department;
import ru.progressnw.employees.model.Role;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentService departmentService;

    public String getLoggedUsername() {
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }
        return username;
    }

    public User addManagerRole(User user, Department department) {
        Set<Role> roles = user.getRoles();
        roles.add(Role.MANAGER);
        user.setRoles(roles);
        user.setDepartment(department);
        return user;
    }

    public User removeManagerRole(User user) {
        Set<Role> roles = user.getRoles();
        roles.remove(Role.MANAGER);
        user.setRoles(roles);
        return user;
    }

    public List<User> getUsersByManager(User manager) {
        List<User> users = new ArrayList<>(Collections.emptyList());
        List<Department> departments = departmentService.getDepartmentListByManager(manager);
        departments.forEach(department -> users.addAll(userRepository.findAllByDepartment(department)));
        return users;
    }
}

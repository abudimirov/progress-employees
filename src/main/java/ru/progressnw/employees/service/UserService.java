package ru.progressnw.employees.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.progressnw.employees.model.Department;
import ru.progressnw.employees.model.Role;
import ru.progressnw.employees.model.User;

import java.util.Set;

@Service
public class UserService {
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
}

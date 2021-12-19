package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.progressnw.employees.model.Department;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.UserRepository;
import ru.progressnw.employees.service.DepartmentService;
import ru.progressnw.employees.service.ResponsibilityService;
import ru.progressnw.employees.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ResponsibilityService responsibilityService;
    private final DepartmentService departmentService;

    @GetMapping("/manager")
    public String showUserList(Model model) {
        User manager = userRepository.findByUsername(userService.getLoggedUsername());
        List<User> usersList = userService.getUsersByManager(manager);
        model.addAttribute("managedDepartments", departmentService.getDepartmentListByManager(manager));
        model.addAttribute("users", usersList);
        model.addAttribute("responsibilities", responsibilityService.getResponsibilityListByUsers(usersList));
        return "manager";
    }
}

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

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ResponsibilityService responsibilityService;
    private final DepartmentService departmentService;

    @Resource(name = "filteredUsers")
    private Map<String, List<User>> filteredUsersList;

    @GetMapping("/manager")
    public String showUserList(Model model) {
        User manager = userRepository.findByUsername(userService.getLoggedUsername());
        List<User> usersList = userService.getUsersByManager(manager);
        model.addAttribute("managedDepartments", departmentService.getDepartmentListByManager(manager));
        model.addAttribute("users", usersList);
        model.addAttribute("responsibilities", responsibilityService.getResponsibilityListByUsers(usersList));
        model.addAttribute("filteredResponsibility", responsibilityService.getResponsibilityListByUsers(filteredUsersList.getOrDefault(userService.getLoggedUsername(), Collections.emptyList())));
        model.addAttribute("filteredUsers", filteredUsersList.getOrDefault(userService.getLoggedUsername(), Collections.emptyList()));
        return "manager";
    }
}

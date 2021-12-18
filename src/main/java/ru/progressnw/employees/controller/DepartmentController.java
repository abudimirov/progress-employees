package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.progressnw.employees.model.Department;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.DepartmentRepository;
import ru.progressnw.employees.repository.UserRepository;
import ru.progressnw.employees.service.DepartmentService;
import ru.progressnw.employees.service.UserService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DepartmentController {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserService userService;
    private final DepartmentService departmentService;

    @GetMapping("/admin/departments")
    public String showNewDepartmentForm(Department department, Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
        return "departments/main-departments";
    }

    @PostMapping("/admin/departments")
    public String addNewDepartment(@Valid Department department, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("departments", departmentRepository.findAll());
            return "departments/main-departments";
        }
        Optional<User> userInDb = userRepository.findById(department.getManager().getId());
        userInDb.ifPresent(userService::addManagerRole);
        departmentRepository.save(department);
        return "redirect:/admin/departments";
    }

    @GetMapping("/admin/departments/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        model.addAttribute("department", department);
        model.addAttribute("users", userRepository.findAll());
        return "departments/update-department";
    }

    @PostMapping("/admin/departments/{id}")
    public String updateDepartment(@PathVariable("id") long id, @Valid Department department,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            department.setId(id);
            model.addAttribute("users", userRepository.findAll());
            return "departments/update-department";
        }
        User previousAdmin = departmentRepository.findById(id).get().getManager();
        if (!departmentService.userIsAnotherDepartmentAdmin(previousAdmin, department)) {
            userService.removeManagerRole(previousAdmin);
        }
        Optional<User> userInDb = userRepository.findById(department.getManager().getId());
        if (userInDb.isPresent()) {
            department.setManager(userInDb.get());
            userService.addManagerRole(userInDb.get());
        }
        departmentRepository.save(department);
        return "redirect:/admin/departments";
    }
}

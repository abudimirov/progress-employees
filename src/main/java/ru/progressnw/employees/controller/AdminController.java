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
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.DepartmentRepository;
import ru.progressnw.employees.repository.ResponsibilityRepository;
import ru.progressnw.employees.repository.UserRepository;
import ru.progressnw.employees.service.ResponsibilityService;
import ru.progressnw.employees.util.ResponsibilityExcelExporter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final ResponsibilityRepository responsibilityRepository;
    private final DepartmentRepository departmentRepository;
    private final ResponsibilityService responsibilityService;

    @Resource(name = "filteredUsers")
    private List<User> filteredUsersList;

    @GetMapping("/admin")
    public String showUserList(Model model) {
        model.addAttribute("users", userRepository.findAllByOrderByLastname());
        model.addAttribute("responsibilities", responsibilityRepository.findAll(Sort.by(Sort.Direction.ASC, "user.lastname")
            .and(Sort.by(Sort.Direction.ASC, "description"))));
        model.addAttribute("filteredResponsibility", responsibilityService.getResponsibilityListByUsers(filteredUsersList));
        model.addAttribute("filteredUsers", filteredUsersList);
        return "admin";
    }

    @GetMapping("/responsibilities/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=responsibilities_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Responsibility> listResponsibility = responsibilityService.getResponsibilityListByUsers(filteredUsersList);

        ResponsibilityExcelExporter excelExporter = new ResponsibilityExcelExporter(listResponsibility);

        excelExporter.export(response);
    }

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
        Optional<User> userInDb = userRepository.findById(department.getManager().getId());
        userInDb.ifPresent(department::setManager);
        departmentRepository.save(department);
        return "redirect:/admin/departments";
    }
}

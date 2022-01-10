package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.ResponsibilityRepository;
import ru.progressnw.employees.repository.UserRepository;
import ru.progressnw.employees.service.ResponsibilityService;
import ru.progressnw.employees.service.UserService;
import ru.progressnw.employees.util.ResponsibilityExcelExporter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final ResponsibilityRepository responsibilityRepository;
    private final ResponsibilityService responsibilityService;
    private final UserService userService;

    @Resource(name = "filteredUsers")
    private Map<String, List<User>> filteredUsersList;

    @GetMapping("/admin")
    public String showUserList(Model model) {
        model.addAttribute("users", userRepository.findAllByOrderByDepartmentNameAscLastnameAsc());
        model.addAttribute("responsibilities", responsibilityRepository.findAll(Sort.by(Sort.Direction.ASC, "user.lastname")
            .and(Sort.by(Sort.Direction.ASC, "description"))));
        model.addAttribute("filteredResponsibility", responsibilityService.getResponsibilityListByUsers(filteredUsersList.getOrDefault(userService.getLoggedUsername(), Collections.emptyList())));
        model.addAttribute("filteredUsers", filteredUsersList.getOrDefault(userService.getLoggedUsername(), Collections.emptyList()));
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

        List<Responsibility> listResponsibility = responsibilityService.getResponsibilityListByUsers(filteredUsersList.get(userService.getLoggedUsername()));

        ResponsibilityExcelExporter excelExporter = new ResponsibilityExcelExporter(listResponsibility);

        excelExporter.export(response);
    }
}

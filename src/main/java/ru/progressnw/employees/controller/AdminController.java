package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.ResponsibilityRepository;
import ru.progressnw.employees.repository.UserRepository;
import ru.progressnw.employees.service.ResponsibilityService;
import ru.progressnw.employees.util.ResponsibilityExcelExporter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final ResponsibilityRepository responsibilityRepository;

    @Autowired
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

}

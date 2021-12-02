package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.ResponsibilityRepository;
import ru.progressnw.employees.repository.UserRepository;
import ru.progressnw.employees.service.ResponsibilityService;

import javax.annotation.Resource;
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

        return "admin";
    }
}

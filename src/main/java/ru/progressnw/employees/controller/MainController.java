package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.ResponsibilityRepository;
import ru.progressnw.employees.repository.UserRepository;
import ru.progressnw.employees.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserRepository userRepository;
    private final ResponsibilityRepository responsibilityRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showMyResponsibilityList(Model model) {
        String loggedUsername = userService.getLoggedUsername();
        List<Responsibility> responsibility;
        List<Responsibility> deputyResponsibility;
        if (!loggedUsername.isEmpty()) {
            User userInDb = userRepository.findByUsername(loggedUsername);
            model.addAttribute("loggedInUser", userInDb);

            responsibility = new ArrayList<>();
            responsibilityRepository.findByUser(userInDb).forEach(responsibility::add);
            deputyResponsibility = new ArrayList<>();
            responsibilityRepository.findByDeputy(userInDb).forEach(deputyResponsibility::add);
        } else {
            responsibility = Collections.emptyList();
            deputyResponsibility = Collections.emptyList();
        }
        model.addAttribute("myResponsibility", responsibility);
        model.addAttribute("deputyResponsibility", deputyResponsibility);
        return "index";
    }
}

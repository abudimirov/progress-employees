package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.ResponsibilityRepository;
import ru.progressnw.employees.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserRepository userRepository;
    private final ResponsibilityRepository responsibilityRepository;

    @GetMapping("/")
    public String showMyResponsibilityList(Model model) {
        String loggedUsername = getLoggedUsername();
        List<Responsibility> responsibility;
        if (!loggedUsername.isEmpty()) {
            User userInDb = userRepository.findByUsername(loggedUsername);
            responsibility = new ArrayList<>();
            responsibilityRepository.findByUser(userInDb).forEach(responsibility::add);
            model.addAttribute("loggedInUser", userInDb);
        } else {
            responsibility = Collections.emptyList();
        }
        model.addAttribute("myResponsibility", responsibility);
        return "index";
    }

    private String getLoggedUsername() {
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }
        return username;
    }
}

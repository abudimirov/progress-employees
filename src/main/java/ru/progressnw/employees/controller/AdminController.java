package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.progressnw.employees.repository.ResponsibilityRepository;
import ru.progressnw.employees.repository.UserRepository;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final ResponsibilityRepository responsibilityRepository;

    @GetMapping("/admin")
    public String showUserList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("responsibilities", responsibilityRepository.findAll());

        return "admin";
    }
}

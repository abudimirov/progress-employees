package ru.progressnw.employees.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.progressnw.employees.model.Role;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.UserRepository;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (result.hasErrors() || userFromDB != null) {
            return "auth/registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/admin";
    }
}

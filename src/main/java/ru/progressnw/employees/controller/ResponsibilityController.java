package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.ResponsibilityRepository;
import ru.progressnw.employees.repository.UserRepository;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ResponsibilityController {
    private final ResponsibilityRepository responsibilityRepository;
    private final UserRepository userRepository;

    @GetMapping("/add-responsibility")
    public String showNewResponsibilityForm(Responsibility responsibility, Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "add-responsibility";
    }

    @PostMapping("/add-responsibility")
    public String addNewResponsibility(@Valid Responsibility responsibility, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-responsibility";
        }

        responsibilityRepository.save(responsibility);
        return isAdmin() ? "redirect:/admin" : "redirect:/";
    }

    @GetMapping("/edit-responsibility/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Responsibility responsibility = responsibilityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid responsibility Id:" + id));
        model.addAttribute("responsibility", responsibility);
        model.addAttribute("users", userRepository.findAll());
        return "update-responsibility";
    }

    @PostMapping("/update-responsibility/{id}")
    public String updateResponsibility(@PathVariable("id") long id, @Valid Responsibility responsibility,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            responsibility.setId(id);
            return "update-responsibility";
        }
        Optional<User> userInDb = userRepository.findById(responsibility.getUser().getId());
        userInDb.ifPresent(responsibility::setUser);

        responsibilityRepository.save(responsibility);
        return isAdmin() ? "redirect:/admin" : "redirect:/";
    }

    @GetMapping("/delete-responsibility/{id}")
    public String deleteResponsibility(@PathVariable("id") long id, Model model) {
        Responsibility responsibility = responsibilityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid responsibility Id:" + id));
        responsibilityRepository.delete(responsibility);
        return isAdmin() ? "redirect:/admin" : "redirect:/";
    }

    private boolean isAdmin() {
        boolean isAdmin = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            isAdmin = authentication.getName().equals("admin");
        }
        return isAdmin;
    }
}

package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.repository.ResponsibilityRepository;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ResponsibilityController {
    private final ResponsibilityRepository responsibilityRepository;

    @GetMapping("/add-responsibility")
    public String showNewResponsibilityForm(Responsibility responsibility) {
        return "add-responsibility";
    }

    @PostMapping("/add-responsibility")
    public String addNewResponsibility(@Valid Responsibility responsibility, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-responsibility";
        }

        responsibilityRepository.save(responsibility);
        return "redirect:/";
    }
}

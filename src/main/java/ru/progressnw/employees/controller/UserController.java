package ru.progressnw.employees.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.progressnw.employees.model.Role;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.UserRepository;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @Resource(name = "filteredUsers")
    private List<User> filteredUsersList;

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

    @GetMapping("/users/{id}")
    public String showUserUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Неверный user id:" + id));

        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "edit-user";
        }
        //TODO Убрать хардкод одной роли
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/disable/{id}")
    public String disableUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setActive(false);
        userRepository.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/filter/{id}")
    public String filterUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        if (filteredUsersList.contains(user)) {
            filteredUsersList.remove(user);
        } else {
            filteredUsersList.add(user);
        }
        return "redirect:/admin";
    }
}

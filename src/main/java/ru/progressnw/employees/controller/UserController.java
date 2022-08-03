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
import ru.progressnw.employees.repository.DepartmentRepository;
import ru.progressnw.employees.repository.UserRepository;
import ru.progressnw.employees.service.UserService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserService userService;

    @Resource(name = "filteredUsers")
    private Map<String, List<User>> filteredUsersList;

    @GetMapping("/registration")
    public String registration(User user, Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
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
        model.addAttribute("departments", departmentRepository.findAll());
        return "edit-user";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "edit-user";
        }
        user.setRoles(userRepository.findById(id).get().getRoles());
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
        List<User> filteredUsers = filteredUsersList
            .getOrDefault(userService.getLoggedUsername(), new ArrayList<>());
        if (filteredUsers.contains(user)) {
            filteredUsers.remove(user);
        } else {
            filteredUsers.add(user);
        }
        filteredUsersList.put(userService.getLoggedUsername(), filteredUsers);
        return userService.isAdmin() ? "redirect:/admin" : "redirect:/manager";
    }

    @GetMapping("/filter/flush")
    public String flushUserFilter() {
        filteredUsersList.put(userService.getLoggedUsername(), Collections.emptyList());
        return userService.isAdmin() ? "redirect:/admin" : "redirect:/manager";
    }
}

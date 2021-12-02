package ru.progressnw.employees.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.ResponsibilityRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ResponsibilityService {

    @Autowired
    private ResponsibilityRepository responsibilityRepository;

    public List<Responsibility> getResponsibilityListByUsers(List<User> users) {
        List<Responsibility> responsibilities = new ArrayList<>();
        for (User user : users) {
            List<Responsibility> temp = responsibilityRepository.findByUser(user);
            responsibilities.addAll(temp);
        }
        return responsibilities;
    }
}

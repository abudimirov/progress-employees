package ru.progressnw.employees.service;

import org.springframework.stereotype.Service;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.ResponsibilityRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponsibilityService {

    private final ResponsibilityRepository responsibilityRepository;

    public ResponsibilityService(ResponsibilityRepository responsibilityRepository) {
        this.responsibilityRepository = responsibilityRepository;
    }

    public List<Responsibility> getResponsibilityListByUsers(List<User> users) {
        List<Responsibility> responsibilities = new ArrayList<>();
        for (User user : users) {
            responsibilities.addAll(responsibilityRepository.findByUser(user));
        }
        return responsibilities;
    }


}

package ru.progressnw.employees.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.ResponsibilityRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponsibilityService {

    private final ResponsibilityRepository responsibilityRepository;

    public List<Responsibility> getResponsibilityListByUsers(List<User> users) {
        List<Responsibility> responsibilities = new ArrayList<>();
        users.forEach(user -> responsibilities.addAll(responsibilityRepository.findByUser(user)));
        
        return responsibilities;
    }


}

package ru.progressnw.employees.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.progressnw.employees.model.Responsibility;
import ru.progressnw.employees.model.User;
import ru.progressnw.employees.repository.ResponsibilityRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ResponsibilityService {

    private final ResponsibilityRepository responsibilityRepository;

    /**
     * Сортировка обязанностей по фамилии пользователя, потом по описанию обязанности
     * @param users
     * @return
     */
    public List<Responsibility> getResponsibilityListByUsers(List<User> users) {
        List<Responsibility> responsibilities = new ArrayList<>();
        users.forEach(user -> responsibilities.addAll(responsibilityRepository.findByUser(user)));
        responsibilities.sort(Comparator.comparing(Responsibility::getUser,
                Comparator.comparing(s -> s.getLastname().toLowerCase(Locale.ROOT)))
            .thenComparing(Responsibility::getDescription));
        return responsibilities;
    }


}

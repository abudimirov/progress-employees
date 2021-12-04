package ru.progressnw.employees.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.progressnw.employees.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * This bean is used for filtration in admin panel. This list is populated with users that admin choose
 */

@Configuration
public class FilteredResponsibility {
    @Bean
    public List<User> filteredUsers() {
        return new ArrayList<>();
    }
}

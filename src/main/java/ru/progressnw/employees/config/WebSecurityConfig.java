package ru.progressnw.employees.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/manager").hasAuthority("MANAGER")
                .antMatchers("/admin/**", "/registration").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
                .logout()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
            .usersByUsernameQuery("select username, password, active from users where username=?")
            .authoritiesByUsernameQuery("select u.username, ur.roles from users u inner join user_role ur on u.user_id = ur.user_id where u.username=?");
        auth.inMemoryAuthentication()
            .withUser("admin_dmitriy").password("{noop}TBxMWbzz").roles("ADMIN");
        auth.inMemoryAuthentication()
            .withUser("admin_victor").password("{noop}G7KfRtqa").roles("ADMIN");
    }
}

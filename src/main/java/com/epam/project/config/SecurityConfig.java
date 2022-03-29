package com.epam.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/* Po pomyślnym zalogowaniu przypisywane są pliki cookie JSESSIONID
Sesja została utworzona dla tego użytkownika
JSESSIONID jest przekazywany do żądania Request, na jego podstawie uzyskujemy dostęp do danych.
Próbowałem dostać się do jednej strony - spadłem na nią po autoryzacji*/

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    // Poprzez konstruktor wprowadzamy userDetailsService i wskazujemy
    // poprzez kwalifikator, że nasza usługa jest potrzebna
    @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Zdefiniuj naszą konfigurację http, skonfiguruj logowanie i wylogowanie
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //crossSiteRequestForgery(mechanizm ochrony przed zagrożeniem csrf)
                .csrf().disable()

                //Do jakich stron użytkownik ma dostęp
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("admin:write")

                .antMatchers("/employee/**").hasAnyAuthority("employee:write","admin:write")
                .antMatchers("/order/**").hasAnyAuthority("employee:write","admin:write")

                //Dostęp dozwolony dla wszystkich użytkowników
                .antMatchers("/furniture/**").permitAll()

                // Dostęp tylko dla niezarejestrowanych użytkowników
                .antMatchers("/register").not().fullyAuthenticated()
                .antMatchers("/login").not().fullyAuthenticated()

                //Dodaj 2 dodatkowe punkty końcowe
                // i włącz nadpisanie formularza autoryzacji
                .and()
                .formLogin()
                //Wszyscy mają dostęp do tej strony
                .loginPage("/login")


                //Jeśli wszystko jest w porządku, przekieruj na stronę
                .defaultSuccessUrl("/furniture")
                .and().
                exceptionHandling().accessDeniedPage("/furniture/forbidden")

                // A także w konfiguracji uwzględniamy
                .and()
                .rememberMe().userDetailsService(userDetailsService)
                .and()
                //Konfiguruj wylogowanie (wylogowanie)
                .logout()
                //logoutRequestMatcher musi być obsługiwany przez
                // AntPathRequestMAthcer i tylko metodę POST
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                // A po wylogowaniu unieważniamy naszą sesję
                .invalidateHttpSession(true)
                // Zniszcz uwierzytelnianie
                .clearAuthentication(true)
                //Usuń cookies
                .deleteCookies("JSESSIONID")
                //Przekieruj do tej strony
                .logoutSuccessUrl("/furniture");
    }

    //Zastąp konfigurację
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Ustaw dla uwierzytelniania zamiast domyślnego DAO Authentication
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    //Zastąp dostawcę uwierzytelniania którego używa baza danych
    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        //Utworzył jego obiekt
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //Ustaw koder hasła passwordEncoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        //Skonfiguruj nasz nadpisany userDetailsService
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        //Zwrócono zmodyfikowany obiekt
        return daoAuthenticationProvider;
    }
}
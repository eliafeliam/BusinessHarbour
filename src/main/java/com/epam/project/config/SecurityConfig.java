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

//При успешном логине присваюваются куки JSESSIONID
//Создаётся сессия для этого пользователя
//в Request запроса передаётся JSESSIONID на его основании получаем доступ к данным.
//Пытались попастьна одну страницу - папали на неё после авторизации

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    //Через конструктор внедряем userDetailsService и указываем через qualifier что нужен именно наш сервис
    @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //Определим свою http конфигурацию, настроим login  и logout
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //crossSiteRequestForgery(механизм защиты от crsf угрозы)
                .csrf().disable()

                //К каким страницам пользватель имеет доступ
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("admin:write")

                .antMatchers("/employee/**").hasAnyAuthority("employee:write","admin:write")

//               Доступ разрешен всем пользователей
                .antMatchers("/furniture/**").permitAll()

                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/register").not().fullyAuthenticated()

                //Добавляем ещё 2 endPoint
                //И включаем переопределение формы авторизации
                .and()
                .formLogin()
                //Все имеют доступ к этой странице
                .loginPage("/login").permitAll()
                
                //Если всё хорошо, перенаправляемся на страницу
                .defaultSuccessUrl("/furniture")

                // И также в конфиге учти:
                .and()
                .rememberMe().userDetailsService(userDetailsService)
                .and()
                //Настроим выход из системы(логаут)
                .logout()
                //logoutRequestMatcher должен быть обработан AntPathRequestMAthcer-ом и метод только POST
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                //А при логауте ИНвалидируем нашу сессию
                .invalidateHttpSession(true)
                //Уничтожь аутентификацию
                .clearAuthentication(true)
                //Удалить куки
                .deleteCookies("JSESSIONID")
                //Перенаправляем на эту страницу
                .logoutSuccessUrl("/furniture");
    }

    //Переопределяем конфигурацию
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Установили для auth вместо стандартного, DAO Authentication
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    //Переобпределим провайдер аутентификации котрый использует БД
    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        //Создали его обьект
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //Задали шифровальшик паролей passwordEncoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        //Установили наш переопределённый userDetailsService
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        //Вернули изменённый обьект
        return daoAuthenticationProvider;
    }
}
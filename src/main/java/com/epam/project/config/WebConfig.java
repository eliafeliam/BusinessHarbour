package com.epam.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;

//Konfigurowanie opcji wyszukiwania plików projektu, widoku
@Configuration
@ComponentScan("com.epam.project")
@EnableWebMvc
// Dziedzicząc po tej klasie otrzymujemy możliwość konfiguracji lokalizacji zasobów.
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Autowired
    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
    // Odpowiedzialny za wyświetlanie widoków
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");

        registry.viewResolver(resolver);
    }
    //Połączenie z DB
    @Bean
    public DataSource dataSource() {
        //W celu zaimplementowania ścieżki do wymaganej bazy danych dla jdbcTemplate tworzymy tę
        // metodę z typem DatSource i zestaw wskaźników.
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // Podaj nazwę sterownika
        dataSource.setDriverClassName("org.postgresql.Driver");
        // Baza danych jest lokalna, tutaj podajemy do niej adres i tabelę
        // z którą będziemy pracować
        dataSource.setUrl("jdbc:postgresql://localhost:5432/EpamFurnitureScore");
        dataSource.setUsername("postgres");
        dataSource.setPassword("1234");
    //Utworzymy ziarno dataSource/ Teraz możemy stworzyć ziarno JdbcTemplate
        return dataSource;
    }

    @Bean
    @Scope("prototype")
    public JdbcTemplate jdbcTemplate() {
        //Tu tworzymy getter, zwracamy połączony obiekt klasy JdbcTemplate.
        return new JdbcTemplate(dataSource());
    }

    //addResourceHandlers(ResourceHandlerRegistry registry)
    // zastępując tę metodę, będziemy mogli określić, gdzie będą znajdować się
    // zasoby naszego projektu, takie jak css, images, js i inne.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/");
    }
}
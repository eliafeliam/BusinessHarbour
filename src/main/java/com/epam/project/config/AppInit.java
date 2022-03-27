package com.epam.project.config;

import com.epam.project.config.SecurityConfig;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

//Aby użyć konfiguracji Spring opartej na Javie,
//ustawiamy ApplicationContext i rejestrujemy konfigurację w Spring Context
public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    //Brak kontekstowego katalogu głównego
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }
    // Dwie klasy konfiguracyjne, podstawowe zadania i bezpieczeństwo
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                WebConfig.class, SecurityConfig.class
        };
    }
    //przetwarzanie których żądań odbywa się w określonych kontekstach
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    //To jest filtr dla Springa, który przetworzy ukryte pole _method, na przykład z żądaniem łatania/usuwania/aktualizacji,
    //i będzie zmieńiac to na żądanie POST dla html 5
    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }
    //Skonfiguruj również dla metody _method
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null ,true, "/*");
    }
}
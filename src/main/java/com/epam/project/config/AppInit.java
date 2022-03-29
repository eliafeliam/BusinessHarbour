package com.epam.project.config;

import com.epam.project.config.SecurityConfig;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

//Для исплользования конфигурации Spring на основе Java
//Задаём ApplicationContext
//Теперь нужно зарегистрировать конфигурацию в Spring Context
public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    //Корнегого контекста нет
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }
    //Два конфигурационных класса, основные задачи и безопасность
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                WebConfig.class, SecurityConfig.class
        };
    }
    //обработка каких запросов осуществляется с помощью опрдеонённых контекстов
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    //Это фильтр для Spring, который будет обрабатывать скрытое(hidden) поле _method например с patch/delete/update запросом, и будет
    //превращать его в POST запрос для html 5
    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }
    //Также конфигурация для _method
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null ,true, "/*");
    }
}
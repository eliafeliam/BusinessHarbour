package project.controller;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    //Проверка правильности редиректа при неправильном логине на неправильные данные пользователя
    public void badCredentials() throws Exception {
        //Обращение, мы можем также использовать formLogin но для наглядности попробуем отправить запрос
        //вручную.Импортируем метод статический для обращения к страницам методом post и указываем что
        //хотим на страницу login отправлять запрос в котором будет параметр user
        this.mockMvc.perform(formLogin().user("admin@mail.com").password( "ADMIN"))
                //в консоль то, что вернул сервер
                .andDo(print())
                //Ожидаем что статус будет в этот раз не ОК и не РЕДИРЕКТ а isForbidden 403
                .andExpect(redirectedUrl("/login?error"));
    }
    //тесты будут запускаться на той же БД которая указана в конфигурации ApplicationProperties
    @Test
//  @Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void correctLoginTest() throws Exception {
        //Также через mocMvc будем проверять авторизацию пользователя через метод который предоставля-
        //ется тестовым фрейворком (formLogin) у нас подключен builder для обращения к форме логина
        //SpringSecurity (статический импорт)
        //Данный метод смотрит как мы в контексте оределили loginPage и вызывает обращение к этой стра
        //нице. Также можно указать имя пользователя и известный нам пароль
        this.mockMvc.perform(formLogin().user("admin@mail.com").password("admin"))
                //Проверяем что у нас корректный ответ сервера, статус ожидаем также redirection
                //но с небольшим отличием
                .andExpect(status().is3xxRedirection())
                //Ожидаем что редирект URL будет на главную страницу сайта
                .andExpect(redirectedUrl("/furniture"));
    }

    @WithUserDetails(value = "admin@mail.com")
    @Test
    public void mainPageTest() throws Exception {
        //Снова работаем с этим запросом, обращаемся к mainPage
        this.mockMvc.perform(get("/furniture"))
                .andDo(print())
                .andExpect(authenticated().withUsername("admin@mail.com"));
    }
}
package project.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FurnitureControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    //Тестовый фреймворк для springBoot поддерживает методы для проверки авторизации
    //Проверяем что приложение потребует от пользователя авторизации в случае если он не авторизован
    public void accessDeniedTest() throws Exception {
        //Указываем адрес страницы которая точно требует авторизации
        this.mockMvc.perform(get("/employee"))
                .andDo(print())
                //Что бы не указывать статус по кодам, упростим и сделем 300, система будет нас пере-
                //Направлять нас страницу логина (кажется 302 код)
                //И проверяем что система у нас ожидает статус отличный от 200
                .andExpect(status().is3xxRedirection())
                //Проверим что система подкинет конкретный адрес
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
package project.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
//Билдер для обращения к форме логина SpringSecurity
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StartPageControllerTest {

    @Autowired
    MockMvc mockMvc;

        @Test
        public void contextLoads() throws Exception {
            this.mockMvc.perform(get("/furniture"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("main/mainPage"));

        }
    @WithUserDetails(value = "admin@mail.com")
    @Test
    public void mainPageTest() throws Exception {
        //Снова работаем с этим запросом, обращаемся к mainPage
        this.mockMvc.perform(get("/furniture"))
                .andDo(print())
                .andExpect(authenticated());
    }
}
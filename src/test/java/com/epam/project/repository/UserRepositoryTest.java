package com.epam.project.repository;


import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.epam.project.model.UserEntity;


public class UserRepositoryTest {
    //Аннотация определяет что будет мокироваться
    @Mock
    UserRepository userRepository;

    @Test
    public void findByEmail_Should_Return_True() {
        //Чтобы заглушка была найдена, нужна аннотация
        MockitoAnnotations.initMocks(this);
        given(userRepository.findByEmail("admin@mail.com")).willReturn
                (Optional.of(new UserEntity("admin@mail.com")));

        Optional<UserEntity> userExist = userRepository.findByEmail("admin@mail.com");

        assertThat(userExist).isPresent();
    }
    @Test
    public void findByEmail_Should_Return_False() {
        //Чтобы заглушка была найдена, нужна аннотация
        MockitoAnnotations.initMocks(this);
        given(userRepository.findByEmail("admin@mail.com")).willReturn(Optional.empty());

        Optional<UserEntity> userExist = userRepository.findByEmail("admin@mail.com");

        assertThat(userExist).isEmpty();
    }
    @Test(expected = Exception.class)
    public void findByEmail_Should_Throw_Exception() {
        //Чтобы заглушка была найдена, нужна аннотация
        MockitoAnnotations.initMocks(this);
        given(userRepository.findByEmail(any(String.class))).willThrow(Exception.class);
        Optional<UserEntity> userExist = userRepository.findByEmail("admin@mail.com");
    }
}
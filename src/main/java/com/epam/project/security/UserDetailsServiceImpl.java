package com.epam.project.security;

import com.epam.project.model.UserEntity;
import com.epam.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Реализуем объект UserDetailsService из SpringSecurity и определим по своему
@Service("userDetailsServiceImpl") //Qualifier name в скобках указываем
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Преобразование пользовательской сущности пользователя, в безопасную.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Сначала получаем нашего userEntity-a из нашего пакета, если нету - исключение
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("UserEntity doesn't exists"));
        //Если есть - возвращаем преобразованный из нашего в Security обьект UserEntity
        return SecurityUser.fromUser(userEntity);
    }

}
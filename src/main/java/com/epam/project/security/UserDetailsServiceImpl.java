package com.epam.project.security;

import com.epam.project.model.UserEntity;
import com.epam.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Zaimplementuj obiekt UserDetailsService z Spring Security i zdefiniuj go po swojemu
@Service("userDetailsServiceImpl") //Qualifier name wskaż w nawiasach
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Transformacja encji niestandardowych użytkownika w bezpieczną.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Po pierwsze, z naszego pakietu otrzymujemy Entity użytkownika, jeśli nie - wyjątek
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("UserEntity doesn't exists"));
        //Jeśli tak, zwracamy obiekt User Entity przekonwertowany z naszego do Security
        return SecurityUser.fromUser(userEntity);
    }
}
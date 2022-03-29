package com.epam.project.security;

import com.epam.project.model.Status;
import com.epam.project.model.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//Реализуем объект UserEntity из SpringSecurity и определим по своему
//Цель - преобразовать нашего UserEntity-а в того, с которым работает Security
@Data
public class SecurityUser implements UserDetails {

    //Определяем поля что есть в UserDetails
    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    public SecurityUser(String username, String password,
                        List<SimpleGrantedAuthority> authorities, boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    //Приводим нашего пользователя в соответствие с SpringSecurity
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    //Имя пользователя
    @Override
    public String getUsername() {
        return username;
    }

    //Пароль
    @Override
    public String getPassword() {
        return password;
    }

    //Не истёк ли срок действия учётной записи
    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    //Заблокирован или разблокирован пользователь.
    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    //Истек ли срок действия учетных данных пользователя (пароля).
    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    //Указывает, включен или отключен пользователь.
    @Override
    public boolean isEnabled() {
        return isActive;
    }

    //Преобразование нашего UserEntity в User Security
    public static UserDetails fromUser(UserEntity userEntity) {
        return new org.springframework.security.core.userdetails.User(
                //В качестве имя передаём email,
                userEntity.getEmail(), userEntity.getPassword(),
                //Включен или отключен пользователь
                userEntity.getStatus().equals(Status.ACTIVE),
                //Спрок действия учётной записи
                userEntity.getStatus().equals(Status.ACTIVE),
                //Срок действия пароля
                userEntity.getStatus().equals(Status.ACTIVE),
                //Не заблокирован ли
                userEntity.getStatus().equals(Status.ACTIVE),
                //Список разрешений
                userEntity.getRole().getAuthorities()
        );
    }
}
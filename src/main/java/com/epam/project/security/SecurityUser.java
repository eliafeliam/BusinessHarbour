package com.epam.project.security;

import com.epam.project.model.Status;
import com.epam.project.model.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//Implementujemy obiekt UserEntity ze SpringSecurity i definiujemy go po swojemu
//Celem jest przekształcenie naszego UserEntity w ten, z którym współpracuje Security
@Data
public class SecurityUser implements UserDetails {

    // Definiujemy pola co jest w UserDetails
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

    //Dostosuj naszego użytkownika do Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    //Nazwa użytkownika
    @Override
    public String getUsername() {
        return username;
    }

    //Hasło
    @Override
    public String getPassword() {
        return password;
    }

    //Czy konto wygasło
    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    //Zablokowany lub odblokowany użytkownik.
    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    //Czy poświadczenia użytkownika (hasło) wygasły.
    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    //Wskazuje, czy użytkownik jest włączony czy wyłączony.
    @Override
    public boolean isEnabled() {
        return isActive;
    }

    //Przekształć nasz UserEntity w Bezpieczeństwo użytkownika
    public static UserDetails fromUser(UserEntity userEntity) {
        return new org.springframework.security.core.userdetails.User(
                // Podaj e-mail jako nazwę
                userEntity.getEmail(), userEntity.getPassword(),
                //Włączony lub wyłączony użytkownik
                userEntity.getStatus().equals(Status.ACTIVE),
                // Data wygaśnięcia konta
                userEntity.getStatus().equals(Status.ACTIVE),
                //Data ważności hasła
                userEntity.getStatus().equals(Status.ACTIVE),
                // Czy jest zablokowany?
                userEntity.getStatus().equals(Status.ACTIVE),
                //Lista uprawnień
                userEntity.getRole().getAuthorities()
        );
    }
}
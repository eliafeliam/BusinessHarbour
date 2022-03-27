package com.epam.project.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Set;
import java.util.stream.Collectors;


//Lista ról użytkownika, administratora, pracownika
public enum Role {
    // Każda rola ma listę uprawnień (które mają atrybuty odczytu/zapisu)
    USER(Set.of(Permission.READ)),
    EMPLOYEE(Set.of(Permission.EMPLOYEE_WRITE, Permission.READ)),
    ADMIN(Set.of(Permission.READ, Permission.ADMIN_WRITE));

    // Każda rola ma ustawione uprawnienia
    private final Set<Permission> permissions;

    //Zaakceptuj wszystkie istniejące uprawnienia
    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    // Oddać je
    public Set<Permission> getPermissions() {
        return permissions;
    }
    /*Na podstawie uprawnień otrzymujemy GrandAuthority, z którym współpracuje ochrona.
     Musimy przekonwertować nasze uprawnienia na SimpleGrantedAuthority */
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}

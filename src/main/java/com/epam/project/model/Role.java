package com.epam.project.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Set;
import java.util.stream.Collectors;


//У нас есть роли
public enum Role {
    //У каждой роли есть список разрешений( у которых есть атрибуты чтения/записи)
    USER(Set.of(Permission.READ)),
    EMPLOYEE(Set.of(Permission.EMPLOYEE_WRITE, Permission.READ)),
    ADMIN(Set.of(Permission.READ, Permission.ADMIN_WRITE));

    //У каждой роли есть set разрешений
    private final Set<Permission> permissions;

    //Принимаем все существующие разрешения
    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    //Отдаём их
    public Set<Permission> getPermissions() {
        return permissions;
    }
    /*На основании permissions(разрешений) получаем GrandAuthority с которыми работает security.
    Необходимо наши разрашения конвертировать в обьекты SimpleGrantedAuthority*/
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}

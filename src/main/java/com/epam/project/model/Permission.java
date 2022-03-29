package com.epam.project.model;

//Перечисление прав
public enum Permission {
    //Разрешения, включающие в себя атрибуты правконкретной роли
    READ("read"),
    ADMIN_WRITE("admin:write"),
    EMPLOYEE_WRITE("employee:write");

    //Обьект разрешений
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

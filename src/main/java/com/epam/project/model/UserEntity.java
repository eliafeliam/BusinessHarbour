package com.epam.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

//Геттеры сетеры

@Data
@AllArgsConstructor
@Component
//Сущность в БД
@Entity
//Связь с таблицей users
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть не менее 2 букв и не более 30")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 30, message = "Фамилия не может быть меньше 2 символов и больше 30")
    private String lastName;

    @Column(name = "email") //Привязываем к названиям реальных столбцов из БД
    @NotEmpty(message = "Email необходимо заполнить")
    @Email(message = "Некорректный Email")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Пароль не может быть пустым")
    private String password;

    @Transient
    private String passwordConfirm;


    @Enumerated(value = EnumType.STRING) //Отметили enum
    @Column(name = "role")
    private Role role;


    @Enumerated(value = EnumType.STRING) //Отметили enum
    @Column(name = "status")
    private Status status;

    public UserEntity() {

    }

    public UserEntity(String email) {
        this.email = email;
    }
}
package com.epam.project.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@Table(name = "calls")
public class CallMe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idCall;

    @Column(name = "first_name")
    @NotEmpty(message = "Пожалуйста, заполните имя")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "email") //Привязываем к названиям реальных столбцов из БД
    @NotEmpty(message = "Email необходимо заполнить")
    @Email(message = "Некорректный Email")
    String email;

    @NotEmpty(message = "Телефон необходимо заполнить")
    @Column(name = "phone_number")
    String phoneNumber;

    public CallMe() {
    }
}

package com.epam.project.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
//Model jest wymagany do dodania żądania
//połączenia od użytkownika
@Entity
@Data
@Table(name = "calls")
public class CallMe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idCall;

    @Column(name = "first_name")
    @NotEmpty(message = "Proszę podać nazwę")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    // Powiąż z nazwami rzeczywistych kolumn z bazy danych
    @Column(name = "email")
    @NotEmpty(message = "Email należy wypełnić")
    @Email(message = "Некорректный Email")
    String email;

    @NotEmpty(message = "Telefon należy wypełnić")
    @Column(name = "phone_number")
    String phoneNumber;

    public CallMe() {
    }
}

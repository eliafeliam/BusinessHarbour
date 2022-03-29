package com.epam.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
//Encja dla użytkownika Spring
//Gettery setery
@Data
@AllArgsConstructor
@Component
//Podmiot w bazie danych
@Entity
//Relacje z tabelą użytkowników
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Powiąż z nazwami rzeczywistych kolumn z bazy danych
    @Column(name = "first_name")
    @NotEmpty(message = "Nazwa nie może być pusta")
    @Size(min = 2, max = 30, message = "Imię musi mieć co najmniej 2 litery i nie więcej niż 30")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "Nazwisko nie może być puste")
    @Size(min = 2, max = 30, message = "Nazwisko nie może mieć mniej niż 2 znaki i więcej niż 30")
    private String lastName;

    @Column(name = "email")
    @NotEmpty(message = "Email należy wypełnić")
    @Email(message = "Niepoprawny Email")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Hasło nie może być puste")
    private String password;

    @Transient
    private String passwordConfirm;

    // Określono Typ enum
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;


    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public UserEntity() {}

    public UserEntity(String email) {
        this.email = email;
    }
}
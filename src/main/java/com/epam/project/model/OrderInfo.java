package com.epam.project.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

//Сущность для заказа
@Data
@Entity
@Table(name = "orderInfo")
public class OrderInfo {
    @Id
    @GenericGenerator(name = "randomGen", strategy = "com.epam.project.utilities.RandomIdGenerator")
    @GeneratedValue(generator = "randomGen")
    int idOrder;

    @Column(name = "email") //Привязываем к названиям реальных столбцов из БД
    @NotEmpty(message = "E-mail należy wypełnić")
    @Email(message = "Niepoprawny Email")
    String email;

    @Column(name = "firstName")
    @NotEmpty(message = "")
    @Size(min = 2, max = 30, message = "Nazwa musi mieć co najmniej 2 litery i nie więcej niż 30")
    String firstName;

    @Column(name = "lastName")
    @NotEmpty(message = "")
    @Size(min = 2, max = 30, message = "Nazwisko musi mieć co najmniej 2 litery i nie więcej niż 30")
    String lastName;

    @Column(name = "city")
    @NotEmpty(message = "Trzeba wypełnić kolumnę 'miasto'")
    String city;

    @Column(name = "street")
    @NotEmpty(message = "Trzeba wypełnić kolumnę 'ulica'")
    String street;

    @Column(name = "house")
    @NotEmpty(message = "Trzeba wypełnić kolumnę 'Dom'")
    String house;

    @Column(name = "apartment")
    int apartment;

    // Pola oznaczone Transient są potrzebne do ostatecznego
    // wypełnienia pełnych informacji za JDBC Template, a nie Hibernate
    @Transient
    int idProduct;
    @Transient
    int number;
    @Transient
    String title;

    public OrderInfo(String email) {
        this.email = email;
    }

    public OrderInfo() {}
}

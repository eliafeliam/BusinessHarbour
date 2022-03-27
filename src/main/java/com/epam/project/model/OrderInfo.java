package com.epam.project.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

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
    @NotEmpty(message = "Email необходимо заполнить")
    @Email(message = "Некорректный Email")
    String email;

    @Column(name = "firstName")
    @NotEmpty(message = "")
    @Size(min = 2, max = 30, message = "Имя должно быть не менее 2 букв и не более 30")
    String firstName;

    @Column(name = "lastName")
    @NotEmpty(message = "")
    @Size(min = 2, max = 30, message = "Фамилия должна быть не менее 2 букв и не более 30")
    String lastName;

    @Column(name = "city")
    @NotEmpty(message = "Необходимо заполнить колонку 'город'")
    String city;

    @Column(name = "street")
    @NotEmpty(message = "Необходимо заполнить колонку 'улица'")
    String street;

    @Column(name = "house")
    @NotEmpty(message = "Необходимо заполнить колонку 'дом'")
    String house;

    @Column(name = "apartment")
    int apartment;

    //Поля помеченные Transient нужны для конечного заполнения полной информации через DAO, а не Hibernate
    @Transient
    int products_id;
    @Transient
    int number;
    @Transient
    String title;

    public OrderInfo() {
    }

    public OrderInfo(String email) {
        this.email = email;
    }
}

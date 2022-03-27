package com.epam.project.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

import java.util.Objects;

@Data
public class Product {

    private int id;

    @NotEmpty(message = "Tytuł nie może być pusty")
    @Size(min = 2, max = 30, message = "Tytuł musi mieć co najmniej 2 i nie więcej niż 30 znaków")
    private String title;

    private int price;

    private int totalCost;

    @NotEmpty(message = "Wypełnij typ")
    private String type;

    @NotEmpty(message = "Title_image nie może być pusty")
    private String title_image;

    @NotEmpty(message = "Wypełnij opis")
    @Size(min = 20, max = 500, message = "Opis nie może być mniejszy niż 20 i większy niż 500 znaków")
    private String description;

    private int count;


    public Product() {}

    public Product(int id, int count) {
        this.id = id;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
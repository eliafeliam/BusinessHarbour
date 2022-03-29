package com.epam.project.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

import java.util.Objects;

@Data
public class Product {

    private int id;

    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 2, max = 30, message = "Название должно быть не менее 2 и не более 30 символов")
    private String title;

    @Min(value = 0, message = "Цена не может быть 0")
    private int price;

    private int totalCost;

    @NotEmpty(message = "Заполните тип")
    private String type;

    @NotEmpty(message = "Title_image не может быть пустым")
    private String title_image;

    @NotEmpty(message = "Заполните описание")
    @Size(min = 20, max = 500, message = "Описание не может быть меньше 20 и больше 500 символов")
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
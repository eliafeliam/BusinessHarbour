package com.epam.project.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class CartNote {
    List<Product> listOfProducts = new ArrayList<>();
    String idClient;
    int idProduct;
    int count;
    int finalAmount;

    public CartNote() {
    }

    public CartNote(List<Product> listOfProducts, String idClient, int finalAmount) {
        this.listOfProducts = listOfProducts;
        this.idClient = idClient;
        this.finalAmount = finalAmount;
    }
}

package com.epam.project.dao;

import com.epam.project.model.CartNote;
import com.epam.project.model.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartDAO {

    private final JdbcTemplate jdbcTemplate;

    public CartDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getProductsListByEmail(String email) {
        List<Product> productsList = jdbcTemplate.query("SELECT product.id,product.title,product.type, product.price, " +
                        "goods_details.title_image,goods_details.description," +
                        "cart_notes.count FROM product \n" +
                        "INNER JOIN goods_details ON goods_details.id_info = product.id " +
                        "INNER JOIN cart_notes ON cart_notes.id_product = product.id " +
                        "WHERE cart_notes.id_client=?",
                new Object[]{email}, new BeanPropertyRowMapper<>(Product.class));
        return productsList;
    }

    public void addOrIncrementInCart(String idClient, int idProduct) {
        int quantityOfThisProduct = doesItExistInCart(idClient, idProduct);

        if(quantityOfThisProduct>0) {
            jdbcTemplate.update("UPDATE cart_notes SET count=? WHERE id_client=? AND id_product=?",
                    quantityOfThisProduct+1, idClient,idProduct);
        } else {
            jdbcTemplate.update("INSERT INTO cart_notes (id_client,id_product,count) VALUES (?, ?, ?)",
                    idClient, idProduct, 1);
        }
    }

    public void decrementElement(String idClient, int idProduct) {
        int number = doesItExistInCart(idClient, idProduct);
        if(number==0) {
            return;
        } else {
            jdbcTemplate.update("UPDATE cart_notes SET count=? WHERE id_client=? AND id_product=?",
                    number - 1, idClient, idProduct );
        }
    }

    public void removeElement(String idClient, int idProduct) {
        jdbcTemplate.update("DELETE FROM cart_notes WHERE id_client=? AND id_product=?",idClient, idProduct);
    }

    public int doesItExistInCart(String idClient, int idProduct) {
        CartNote countOfElement = jdbcTemplate.query("SELECT count FROM cart_notes " +
                        "WHERE id_client=? AND id_product=?",new Object[]{idClient, idProduct},
                new BeanPropertyRowMapper<>(CartNote.class)).stream().findAny().orElse(null);
        return countOfElement==null? 0 : countOfElement.getCount();
    }
    public void cleanCart(String email) {
        jdbcTemplate.update("DELETE FROM cart_notes" +
                " WHERE id_client=?", email);
    }
}
package com.epam.project.dao;

import com.epam.project.model.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProductDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProductDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getAllProducts() {
        return jdbcTemplate.query("SELECT * FROM product", new BeanPropertyRowMapper<>(Product.class));
    }
    public void save(Product goods) {
        jdbcTemplate.update("INSERT INTO product (title,type, price) VALUES (?, ?, ?);INSERT INTO goods_details (title_image, description) VALUES (?,?) ",
                goods.getTitle(), goods.getType(), goods.getPrice(),goods.getTitle_image(), goods.getDescription());
    }
    public void update(int id, Product updated) {
        jdbcTemplate.update("UPDATE product SET title=?, price=?, type=? WHERE id=?;UPDATE goods_details SET title_image=?, description=? WHERE id_info=?", updated.getTitle(),
                updated.getPrice(), updated.getType(), id,updated.getTitle_image(), updated.getDescription(), id);
    }
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM goods_details WHERE id_info=?;DELETE FROM product WHERE id=?", id,id);
    }
    public List<Product> getAllItemsWithType(String type) {
        List<Product> productsList = jdbcTemplate.query("SELECT product.id,product.title,product.type," +
                        "product.price,product.count,goods_details.title_image,goods_details.description " +
                        "FROM product JOIN goods_details ON goods_details.id_info = product.id WHERE product.type=?",
                new Object[]{type}, new BeanPropertyRowMapper<>(Product.class));
        return productsList;
    }

    public Product getProductById(int id) {
        Product product = jdbcTemplate.query("SELECT product.id,product.title,product.type," +
                        "product.price,product.count,goods_details.title_image,goods_details.description " +
                        "FROM product JOIN goods_details ON goods_details.id_info = product.id WHERE product.id=?",
                new Object[]{id},new BeanPropertyRowMapper<>(Product.class)).stream().findAny().orElse(null);

        return product;
    }
}
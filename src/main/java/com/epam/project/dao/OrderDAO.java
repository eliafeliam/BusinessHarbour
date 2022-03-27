package com.epam.project.dao;

import com.epam.project.model.CartNote;
import com.epam.project.model.OrderInfo;
import com.epam.project.model.Product;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDAO {

    final
    JdbcTemplate jdbcTemplate;

    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Получаем список товаров и их кол-во, назначаем заказу уникальный id, отправляем данные в БД
    public void createOrder(List<Product> productsList, OrderInfo orderInfo) {
        for (Product product : productsList) {
            jdbcTemplate.update("INSERT INTO orders (id_order,email,products_id,number) VALUES (?, ?, ?, ?)",
                    orderInfo.getIdOrder(), orderInfo.getEmail(), product.getId(), product.getCount());
        }
    }
    //Список информации о деталях заказа конкретного заказчика
    public List<OrderInfo> getOrder(int id_order) {
        return jdbcTemplate.query(
                "SELECT order_info.email,order_info.first_name,order_info.last_name, order_info.city,order_info.street,order_info.house,order_info.apartment, order_info.id_order,orders.number,orders.products_id, product.title  FROM order_info INNER JOIN orders ON order_info.id_order = orders.id_order  INNER JOIN product ON orders.products_id = product.id WHERE orders.id_order=?",
                new Object[]{id_order},new BeanPropertyRowMapper<>(OrderInfo.class));
    }
    //Удаление элемента из заказа
    public void removeElement(int idOrder,int idProduct) {
        jdbcTemplate.update("DELETE FROM orders WHERE id_order=? AND products_id=?",idOrder, idProduct);
    }
    //Удаление заказа целиком
    public void removeOrder(int idOrder) {
        jdbcTemplate.update("DELETE FROM orders WHERE id_order=?",
                idOrder);
    }
    //Добавление одного предмета
    public void incrementElement(int idOrder, int idProduct) {
        int number = doesItExistInOrder(idOrder, idProduct);
        jdbcTemplate.update("UPDATE orders SET number=? WHERE products_id=? AND id_order=?",
                number+1, idProduct, idOrder);
    }
    //Удаление одного предмета
    public void decrementElement(int idOrder, int idProduct) {
        int number = doesItExistInOrder(idOrder, idProduct);
        if(number<=0) {
            return;
        } else {
            jdbcTemplate.update("UPDATE orders SET number=? WHERE id_order=? AND products_id=?",
                    number - 1, idOrder, idProduct);
        }
    }
    //Есть ли товар уже в заказе
    public int doesItExistInOrder(int idOrder, int idProduct) {
        OrderInfo orderInfo = jdbcTemplate.query("SELECT number FROM orders WHERE id_order=? AND products_id=?",
                        new Object[]{idOrder, idProduct}, new BeanPropertyRowMapper<>(OrderInfo.class))
                .stream().findAny().orElse(null);
        return orderInfo == null? 0 : orderInfo.getNumber();
    }
}

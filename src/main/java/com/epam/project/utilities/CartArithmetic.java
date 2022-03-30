package com.epam.project.utilities;

import com.epam.project.dao.CartDAO;
import com.epam.project.dao.ProductDAO;
import com.epam.project.model.CartNote;
import com.epam.project.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class CartArithmetic {
    final CartDAO cartDAO;
    final ProductDAO productDAO;

    public CartArithmetic(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public  CartNote getCartForRegistered(String id) {
        //Получили список товаров и их кол-во для данного пользователя
        List<Product> productsList = cartDAO.getProductsListByEmail(id);
        int finalAmount = sumOfCart(productsList);
        CartNote cart = new CartNote(productsList, id, finalAmount);
        return cart;
    }
    public  CartNote getCartForUnRegistered(List<Product> productsList) {
        int finalAmount = sumOfCart(productsList);
        CartNote cart = new CartNote(productsList,"Guest",finalAmount);
        return cart;
    }

    //Посчитать сумму предметов из корзины в списке
    public int sumOfCart(List<Product> productsList) {
        int totalCost = 0;
        int sumOfCart = 0;
        //Посчитать сумму конкретнго предмета умножив цену на кол-во товара в корзине
        for (Product product : productsList) {
            totalCost = product.getCount()*product.getPrice();
            product.setTotalCost(totalCost);
            sumOfCart+=totalCost;
            totalCost = 0;
        }
        return sumOfCart;
    }
    public List<Product> addOrIncrementToCart(int idProduct,List<Product> productsList) {
        Product product = productDAO.getProductById(idProduct);
        if (productsList.contains(product)) {
            Product item = productsList.get(productsList.indexOf(product));
            item.setCount(item.getCount() + 1);
        } else {
            product.setCount(1);
            productsList.add(product);
        }
        return productsList;
    }
}

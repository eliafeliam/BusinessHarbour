package com.epam.project.controller;

import com.epam.project.dao.CartDAO;
import com.epam.project.dao.EmployeeDAO;
import com.epam.project.model.CartNote;
import com.epam.project.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartDAO cartDAO;
    private final EmployeeDAO employeeDAO;


    public CartController(CartDAO cartDAO, EmployeeDAO employeeDAO) {
        this.cartDAO = cartDAO;
        this.employeeDAO = employeeDAO;
    }

    //Посмотреть корзину
    @GetMapping
    public String getCart(@SessionAttribute("productsList") List<Product> productsList,
                          HttpServletRequest request, Model model) {
        Principal principal = request.getUserPrincipal();

        CartNote cart;
        int finalAmount;
        //Авторизован?
        if (principal != null) {
            cart = getCartForRegistered(principal.getName());
        }
        else {
            cart = getCartForUnRegistered(productsList);
        }
        model.addAttribute("cart", cart);
        model.addAttribute("finalAmount", cart.getFinalAmount());
        return "cart/cart";
    }

    //Добавление товара в базу и корзину
    @PostMapping("/addToCart/{idProduct}")
    public String addOrIncrementToCart(@PathVariable("idProduct") int idProduct, HttpServletRequest request,
                                       @SessionAttribute("productsList") List<Product> productsList) {
        Principal principal = request.getUserPrincipal();
        CartNote cartNote;
        // //Для зарегистированного пользователя
        if (principal != null) {
            cartDAO.addOrIncrementInCart(principal.getName(),idProduct);
        }
        //Для незарегестрированного пользователя
        else {
            //Добавляем если нету, удваиваем товар в корзине если уже добавлен
            Product product = employeeDAO.getProductById(idProduct);
            if (productsList.contains(product)) {
                Product item = productsList.get(productsList.indexOf(product));
                item.setCount(item.getCount() + 1);
                //Если продукта ещё нет в корзине
            } else {
                product.setCount(1);
                productsList.add(product);
            }
        }
        return "redirect:/cart";
    }


    //Полное удаление из корзины определённого товара
    @DeleteMapping("/removeElement/{productsID}")
    public String removeElement(@PathVariable("productsID") int idProduct,
                                HttpServletRequest request, Model model,
                                @SessionAttribute("productsList") List<Product> productsList) {
        Principal principal = request.getUserPrincipal();

        //Для неавторизованого пользователя
        if (principal != null) {
            cartDAO.removeElement(principal.getName(), idProduct);
        } else {
            Product product = employeeDAO.getProductById(idProduct);
            //Для неавторизованого пользователя
            if (productsList.contains(product)) {
                productsList.remove(product);
            }
            model.addAttribute("finalAmount", sumOfCart(productsList));
        }
        return "redirect:/cart";
    }

    @PostMapping("/decrementGoods/{idProduct}")
    public String decrementElement(@PathVariable("idProduct") int idProduct,
                                   @SessionAttribute("productsList") List<Product> productsList,HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        //Если авторизован
        if (principal != null) {
            cartDAO.decrementElement(principal.getName(), idProduct);
            //Если не авторизован
        } else {
            Product product = employeeDAO.getProductById(idProduct);
            product = productsList.get(productsList.indexOf(product));
            if (productsList.contains(product) & product.getCount() > 0) {
                product.setCount(product.getCount()-1);
            }
        }
        return "redirect:/cart";
    }

    public CartNote getCartForRegistered(String id) {
        //Получили список товаров и их кол-во для данного пользователя
        List<Product> productsList = cartDAO.getProductsListByEmail(id);
        int finalAmount = sumOfCart(productsList);
        CartNote cart = new CartNote(productsList, id, finalAmount);
        return cart;
    }
    public CartNote getCartForUnRegistered(List<Product> productsList) {
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
}

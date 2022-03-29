package com.epam.project.controller;

import com.epam.project.dao.CartDAO;
import com.epam.project.dao.EmployeeDAO;
import com.epam.project.model.CartNote;
import com.epam.project.model.Product;
import com.epam.project.utilities.CartArithmetic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@SessionAttributes("productsList")
@RequestMapping("/cart")
public class CartController {

    private final CartDAO cartDAO;
    private final EmployeeDAO employeeDAO;
    private final CartArithmetic cartArithmetic;

    public CartController(CartDAO cartDAO, EmployeeDAO employeeDAO, CartArithmetic cartArithmetic) {
        this.cartDAO = cartDAO;
        this.employeeDAO = employeeDAO;
        this.cartArithmetic = cartArithmetic;
    }

    //Посмотреть корзину
    @GetMapping
    public String getCart(@ModelAttribute("productsList") List<Product> productsList,
                          HttpServletRequest request, Model model) {
        Principal principal = request.getUserPrincipal();

        CartNote cart;
        int finalAmount;
        //Авторизован?
        if (principal != null) {
            cart = cartArithmetic.getCartForRegistered(principal.getName());
        }
        else {
            cart = cartArithmetic.getCartForUnRegistered(productsList);
        }
        model.addAttribute("cart", cart);
        model.addAttribute("finalAmount", cart.getFinalAmount());
        return "cart/cart";
    }

    //Добавление товара в базу и корзину
    @PostMapping("/addToCart/{idProduct}")
    public String addOrIncrementToCart(@PathVariable("idProduct") int idProduct, HttpServletRequest request,
                                       @ModelAttribute("productsList") List<Product> productsList) {
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
                                @ModelAttribute("productsList") List<Product> productsList) {
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
            model.addAttribute("finalAmount", cartArithmetic.sumOfCart(productsList));
        }
        return "redirect:/cart";
    }

    @PostMapping("/decrementGoods/{idProduct}")
    public String decrementElement(@PathVariable("idProduct") int idProduct,
                                   @ModelAttribute("productsList") List<Product> productsList,HttpServletRequest request) {
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

    @ModelAttribute("productsList")
    public static List<Product> getProductList() {
        CartNote cartNote = new CartNote();
        List<Product> productsList = cartNote.getListOfProducts();
        return productsList;
    }
}

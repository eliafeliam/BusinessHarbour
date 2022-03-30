package com.epam.project.controller;

import com.epam.project.dao.CartDAO;
import com.epam.project.dao.ProductDAO;
import com.epam.project.model.CartNote;
import com.epam.project.model.Product;
import com.epam.project.utilities.CartArithmetic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@SessionAttributes("productsList")
@RequestMapping("/cart")
public class CartController {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final CartArithmetic cartArithmetic;

    public CartController(CartDAO cartDAO, ProductDAO productDAO, CartArithmetic cartArithmetic) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.cartArithmetic = cartArithmetic;
    }

    @GetMapping
    public String getCart(@ModelAttribute("productsList") List<Product> productsList,
                          HttpServletRequest request, Model model) {
        String email = request.getUserPrincipal() == null? "" : request.getUserPrincipal().getName();

        CartNote cart;
        // Jeśli nie jesteś zalogowany
        if (email.isEmpty()) {
            cart = cartArithmetic.getCartForUnRegistered(productsList);
        }
        else {
            cart = cartArithmetic.getCartForRegistered(email);
        }
        model.addAttribute("cart", cart);
        model.addAttribute("finalAmount", cart.getFinalAmount());
        return "cart/cart";
    }


    @PostMapping("/addToCart/{idProduct}")
    private String addToCart(@PathVariable("idProduct") int idProduct, HttpServletRequest request,
                             @ModelAttribute("productsList") List<Product> productsList) {
        String email = request.getUserPrincipal() == null? "" : request.getUserPrincipal().getName();
        if (email.isEmpty()) {
            cartArithmetic.addOrIncrementToCart(idProduct, productsList);
        }
        // Jeśli nie jest zalogowany
        else {
            cartDAO.addOrIncrementInCart(email,idProduct);
        }
        return "redirect:/cart";
    }


    @DeleteMapping("/removeElement/{productsID}")
    private String removeElement(@PathVariable("productsID") int idProduct,
                                HttpServletRequest request, Model model,
                                @ModelAttribute("productsList") List<Product> productsList) {
        String email = request.getUserPrincipal() == null? "" : request.getUserPrincipal().getName();
        // Jeśli nie jest zalogowany
        if (email.isEmpty()) {
            Product product = productDAO.getProductById(idProduct);
            if (productsList.contains(product)) {
                productsList.remove(product);
            }
            model.addAttribute("finalAmount", cartArithmetic.sumOfCart(productsList));
        } else {
            cartDAO.removeElement(email, idProduct);
        }
        return "redirect:/cart";
    }

    @PostMapping("/decrementGoods/{idProduct}")
    private String decrementElement(@PathVariable("idProduct") int idProduct,
                                   @ModelAttribute("productsList") List<Product> productsList,HttpServletRequest request) {
        String email = request.getUserPrincipal() == null? "" : request.getUserPrincipal().getName();
        // Jeśli nie jest zalogowany
        if (!email.isEmpty()) {
            cartDAO.decrementElement(email, idProduct);
        } else {
            Product product = productDAO.getProductById(idProduct);
            product = productsList.get(productsList.indexOf(product));
            // Aby nie ustawić -1
            if (productsList.contains(product) & product.getCount() > 0) {
                product.setCount(product.getCount()-1);
            }
        }
        return "redirect:/cart";
    }
    // Do koszyka w sesji
    @ModelAttribute("productsList")
    public static List<Product> getProductList() {
        CartNote cartNote = new CartNote();
        List<Product> productsList = cartNote.getListOfProducts();
        return productsList;
    }
}

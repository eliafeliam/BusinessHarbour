package com.epam.project.controller;

import com.epam.project.dao.CartDAO;
import com.epam.project.dao.OrderDAO;
import com.epam.project.model.OrderInfo;
import com.epam.project.model.Product;
import com.epam.project.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {
    final OrderRepository orderRepository;
    final OrderDAO orderDAO;
    final CartDAO cartDAO;

    public OrderController(OrderRepository orderRepository, OrderDAO orderDAO, CartDAO cartDAO) {
        this.orderRepository = orderRepository;
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
    }

    @GetMapping("/getOrderForm")
    private String getOrderForm(Model model, HttpServletRequest request) {
        String email = request.getUserPrincipal() == null? "" : request.getUserPrincipal().getName();
        //Jeśli nie jest zalogowany
        if (email.isEmpty()) {
            model.addAttribute("order", new OrderInfo());
        } else {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setEmail(email);
            model.addAttribute("order", orderInfo);
        }
        return "order/createOrder";
    }

    @PostMapping("/createOrder")
    private String createOrder(@ModelAttribute("order") @Valid OrderInfo orderInfo,
                              BindingResult bindingResult,
                              Model model, HttpServletRequest request) {
        String email = request.getUserPrincipal() == null? "" : request.getUserPrincipal().getName();

        List<Product> productsList;

        if (bindingResult.hasErrors()) {
            return "order/createOrder";
        }
        if (email.isEmpty()) {
            productsList = (List<Product>) request.getSession().getAttribute("productsList");
            orderRepository.save(orderInfo);
            orderDAO.createOrder(productsList, orderInfo);
            //Opróżniamy kosz
            request.getSession().setAttribute("productsList", CartController.getProductList());
        }

        else {
            orderRepository.save(orderInfo);
            productsList = cartDAO.getProductsListByEmail(email);
            orderDAO.createOrder(productsList, orderInfo);
            cartDAO.cleanCart(email);
        }
        model.addAttribute("order", orderInfo);
        return "order/orderCompleted";
    }

    @GetMapping("/getAllOrders")
    private String getAllOrders(Model model) {
        model.addAttribute("ordersList", orderRepository.findAll());
        return "order/allOrders";
    }
    @GetMapping("/getSelectedOrder/{id}")
    public String getOrder(Model model, @PathVariable("id") int idOrder) {
        model.addAttribute("orderParts", orderDAO.getOrder(idOrder));
        return "order/orderParts";
    }
    @DeleteMapping("/removeElement/{idOrder}/{idProduct}")
    private String removeElement(@PathVariable("idOrder") int idOrder, @PathVariable("idProduct") int idProduct) {
        orderDAO.removeElement(idOrder, idProduct);
        return "redirect:/order/getSelectedOrder/" + idOrder;
    }
    @DeleteMapping("/removeOrder/{idOrder}")
    private String removeOrder(@PathVariable("idOrder") int idOrder) {
        orderDAO.removeOrder(idOrder);
        orderRepository.deleteById(idOrder);
        return "redirect:/order/getAllOrders";
    }
    @PatchMapping("/incrementElement/{idOrder}/{idProduct}")
    private String incrementElement(@PathVariable("idOrder") int idOrder, @PathVariable("idProduct") int idProduct) {
        orderDAO.incrementElement(idOrder, idProduct);
        return "redirect:/order/getSelectedOrder/" + idOrder;
    }
    @PatchMapping("/decrementElement/{idOrder}/{idProduct}")
    private String decrementElement(@PathVariable("idOrder") int idOrder, @PathVariable("idProduct") int idProduct) {
        orderDAO.decrementElement(idOrder, idProduct);
        return "redirect:/order/getSelectedOrder/" + idOrder;
    }
}

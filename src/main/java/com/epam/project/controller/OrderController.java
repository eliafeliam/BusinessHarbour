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
import java.security.Principal;
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
    public String getOrderForm(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        //Если не авторизован
        if (principal != null) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setEmail(principal.getName());
            model.addAttribute("order", orderInfo);
        } else {
            model.addAttribute("order", new OrderInfo());
        }
        return "order/createOrder";
    }

    @PostMapping("/createOrder")
    public String createOrder(@ModelAttribute("order") @Valid OrderInfo orderInfo,
                              BindingResult bindingResult,
                              Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        List<Product> productsList;

        if (bindingResult.hasErrors()) {
            return "order/createOrder";
        }
        if (principal != null) {
            orderRepository.save(orderInfo);
            productsList = cartDAO.getProductsListByEmail(principal.getName());
            orderDAO.createOrder(productsList, orderInfo);
            cartDAO.cleanCart(principal.getName());
        }

        else {
            productsList = (List<Product>) request.getSession().getAttribute("productsList");
            orderRepository.save(orderInfo);
            orderDAO.createOrder(productsList, orderInfo);
            request.getSession().setAttribute("productsList", CartController.getProductList());
        }
        model.addAttribute("order", orderInfo);
        return "order/orderCompleted";
    }

    @GetMapping("/getAllOrders")
    public String getAllOrders(Model model) {
        model.addAttribute("ordersList", orderRepository.findAll());
        return "order/allOrders";
    }
    @GetMapping("/getSelectedOrder/{id}")
    public String getOrder(Model model, @PathVariable("id") int idOrder) {
        model.addAttribute("orderParts", orderDAO.getOrder(idOrder));
        return "order/orderParts";
    }
    @DeleteMapping("/removeElement/{idOrder}/{idProduct}")
    public String removeElement(@PathVariable("idOrder") int idOrder, @PathVariable("idProduct") int idProduct) {
        orderDAO.removeElement(idOrder, idProduct);
        return "redirect:/order/getSelectedOrder/" + idOrder;
    }
    @DeleteMapping("/removeOrder/{idOrder}")
    public String removeOrder(@PathVariable("idOrder") int idOrder) {
        orderDAO.removeOrder(idOrder);
        orderRepository.deleteById(idOrder);
        return "redirect:/order/getAllOrders";
    }
    @PatchMapping("/incrementElement/{idOrder}/{idProduct}")
    public String incrementElement(@PathVariable("idOrder") int idOrder, @PathVariable("idProduct") int idProduct) {
        orderDAO.incrementElement(idOrder, idProduct);
        return "redirect:/order/getSelectedOrder/" + idOrder;
    }
    @PatchMapping("/decrementElement/{idOrder}/{idProduct}")
    public String decrementElement(@PathVariable("idOrder") int idOrder, @PathVariable("idProduct") int idProduct) {
        orderDAO.decrementElement(idOrder, idProduct);
        return "redirect:/order/getSelectedOrder/" + idOrder;
    }
}

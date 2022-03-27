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
        //Используется для создания информации о заказчике
        this.orderRepository = orderRepository;
        //Используется для создания заказа и сего подробностями
        this.orderDAO = orderDAO;
        //Получение информации о корзине для заказа
        this.cartDAO = cartDAO;
    }


    @GetMapping("/getOrderForm")
    public String getOrderForm(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        //Если не авторизован
        if (principal != null) {
            //Заполняем преварительно email, во избежание ошибок
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setEmail(principal.getName());
            model.addAttribute("order", orderInfo);
        } else {
            //Пустая форма обьекта для заполнения
            model.addAttribute("order", new OrderInfo());
        }
        return "order/createOrder";
    }

    //Принимаем информацию о заказе
    @PostMapping("/createOrder")
    public String createOrder(@ModelAttribute("order") @Valid OrderInfo orderInfo,
                              @SessionAttribute("productsList") List<Product> productsList,
                              BindingResult bindingResult,
                              Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (bindingResult.hasErrors()) {
            return "order/createOrder";
        }
        //Если пользователь  авторизован
        if (principal != null) {
            //Сохраняем заказ в таблицу с информацией о заказчике с уникальным id
            orderRepository.save(orderInfo);
            //Получаем корзину из БД
            productsList = cartDAO.getProductsListByEmail(principal.getName());
            //создаём заказ в таблице заказов
            orderDAO.createOrder(productsList, orderInfo);
            //Очищаем корзину после успешно офомлненного заказа
            cartDAO.cleanCart(principal.getName());
        }
        //Не авторизован
        else {
            //Сохраняем заказ в таблицу с информацией о заказчике с уникальным id
            orderRepository.save(orderInfo);
            // заносим инфромацию в таблицу заказов
            orderDAO.createOrder(productsList, orderInfo);
            //Обнуляем корзину после оформления заказа
            request.getSession().setAttribute("productsList", StartPageController.getProductList());
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

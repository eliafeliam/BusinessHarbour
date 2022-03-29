//package com.epam.project.controller;
//
//import com.epam.project.dao.CartDAO;
//import com.epam.project.dao.EmployeeDAO;
//import com.epam.project.model.CartNote;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.Arrays;
//import java.util.List;
//
//class CartControllerTest {
//    @MockBean
//    CartDAO cartDAO;
//    @MockBean
//    EmployeeDAO employeeDAO;
//
//    CartController cartController = new CartController(cartDAO, employeeDAO);
//
//    @Test
//    void sumOfItemsList() {
//        List<CartNote> list;
//        CartNote firstCart = new CartNote();
//        CartNote secondCart = new CartNote();
//        CartNote thirdCart = new CartNote();
//        firstCart.setFinalAmount(5);
//        secondCart.setFinalAmount(7);
//        thirdCart.setFinalAmount(13);
//        list = Arrays.asList(firstCart,secondCart,thirdCart);
//
//        int result = cartController.sumOfCart(list);
//        Assertions.assertEquals(25, result);
//    }
//}
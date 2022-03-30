package com.epam.project.controller;

import com.epam.project.model.Product;
import com.epam.project.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.epam.project.dao.ProductDAO;

import javax.validation.Valid;

@Controller
@RequestMapping("/employee")
public class FurnitureController {

    private final ProductDAO productDAO;
    final OrderRepository orderRepository;


    @Autowired
    public FurnitureController(ProductDAO productDAO, OrderRepository orderRepository) {
        this.productDAO = productDAO;
        this.orderRepository = orderRepository;
    }
    @GetMapping()
    private String index(Model model) {
        model.addAttribute("productList", productDAO.getAllProducts());
        return "employee/allProducts";
    }

    @GetMapping("/{id}")
    private String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("selectedElement", productDAO.getProductById(id));
        return "employee/show";
    }

    @GetMapping("/new")
    //ModelAttribute создаёт новый обьект типа Product и передаёт его в модель под ключём goods в представление
    private String newPerson(@ModelAttribute("newProduct") Product goods) {
        return "employee/new";
    }

    @PostMapping()
    private String create(@ModelAttribute("newProduct") @Valid Product goods,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "employee/new";

        productDAO.save(goods);
        return "redirect:/employee";
    }

    @GetMapping("/{id}/edit")
    private String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("selectedElement", productDAO.getProductById(id));
        return "employee/edit";
    }

    @PatchMapping("/{id}")
    private String update(@ModelAttribute("selectedElement") @Valid Product goods,
                          BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "employee/edit";
        }
        productDAO.update(id, goods);
        return "redirect:/employee";
    }

    @DeleteMapping("/{id}")
    private String delete(@PathVariable("id") int id) {
        productDAO.delete(id);
        return "redirect:/employee";
    }
}
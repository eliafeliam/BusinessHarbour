package com.epam.project.controller;

import com.epam.project.model.Product;
import com.epam.project.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.epam.project.dao.EmployeeDAO;

import javax.validation.Valid;

@Controller
@RequestMapping("/employee")
public class FurnitureController {

    private final EmployeeDAO employeeDAO;
    final OrderRepository orderRepository;


    @Autowired
    public FurnitureController(EmployeeDAO employeeDAO, OrderRepository orderRepository) {
        this.employeeDAO = employeeDAO;
        this.orderRepository = orderRepository;
    }
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("productList", employeeDAO.getAllProducts());
        return "product/allProducts";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("selectedElement", employeeDAO.getProductById(id));
        return "product/show";
    }

    @GetMapping("/new")
    //ModelAttribute создаёт новый обьект типа Product и передаёт его в модель под ключём goods в представление
    public String newPerson(@ModelAttribute("newProduct") Product goods) {
        return "product/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("newProduct") @Valid Product goods,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "product/new";

        employeeDAO.save(goods);
        return "redirect:/employee";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("selectedElement", employeeDAO.getProductById(id));
        return "product/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("selectedElement") @Valid Product goods, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "product/edit";

        employeeDAO.update(id, goods);
        return "redirect:/employee";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        employeeDAO.delete(id);
        return "redirect:/employee";
    }
}
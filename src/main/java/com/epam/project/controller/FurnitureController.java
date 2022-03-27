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

    //Подключение к базе данных
    @Autowired
    public FurnitureController(EmployeeDAO employeeDAO, OrderRepository orderRepository) {
        this.employeeDAO = employeeDAO;
        this.orderRepository = orderRepository;
    }
    //Отображение в таблице всех доступных товаров
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("productList", employeeDAO.getAllProducts());
        return "product/allProducts";
    }

    //Получение товара по id
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("selectedElement", employeeDAO.getProductById(id));
        return "product/show";
    }

    //Создание нового товара
    @GetMapping("/new")
    //ModelAttribute создаёт новый обьект типа Product и передаёт его в модель под ключём goods в представление
    public String newPerson(@ModelAttribute("newProduct") Product goods) {
        return "product/new";
    }

    //Создание нового товара в базе данных
    @PostMapping()
    public String create(@ModelAttribute("newProduct") @Valid Product goods,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "product/new";

        employeeDAO.save(goods);
        return "redirect:/employee";
    }

    //Переадресация на страницу с возможностью изменения товара
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("selectedElement", employeeDAO.getProductById(id));
        return "product/edit";
    }

    //Изменение характеристик товара
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("selectedElement") @Valid Product goods, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "product/edit";

        employeeDAO.update(id, goods);
        return "redirect:/employee";
    }

    //Удаление товара из базы данных
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        employeeDAO.delete(id);
        return "redirect:/employee";
    }
}
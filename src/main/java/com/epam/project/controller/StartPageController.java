package com.epam.project.controller;

import com.epam.project.dao.EmployeeDAO;
import com.epam.project.model.CallMe;
import com.epam.project.model.Product;
import com.epam.project.repository.CallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/furniture")

public class StartPageController {

    private final EmployeeDAO employeeDAO;
    private final CallRepository callRepository;

    @Autowired
    public StartPageController(EmployeeDAO employeeDAO, CallRepository callRepository) {
        this.employeeDAO = employeeDAO;
        this.callRepository = callRepository;
    }

    @GetMapping()
    public String showMain() {
        return "main/mainPage";
    }

    @GetMapping("/aboutCompany")
    public String aboutCompany() {
        return "main/aboutCompany";
    }

    @GetMapping("/ourContacts")
    public String ourContacts() {
        return "main/ourContacts";
    }

    @GetMapping("/callMe")
    public String callMe(@ModelAttribute("user") CallMe user) {
        return "main/callMe";
    }

    @PostMapping("/saveCall")
    public String saveCall(@ModelAttribute("user") @Valid CallMe callMe, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "main/callMe";
        }
        callRepository.save(callMe);
        return "redirect:/furniture";
    }

    //Посмотреть все имещиеся кровати
    @GetMapping("/showAll/{type}")
    public String showAllItem(@PathVariable("type") String type, Model model) {
        List<Product> products = employeeDAO.getAllItemsWithType(type);

        model.addAttribute("allItems", products);
        model.addAttribute("type", type);
        return "product/allAboutType";
    }

    //Получить данные о конкретном товаре
    @GetMapping("{id}")
    public String showSelectedElement(@PathVariable("id") int id, Model model) {
        model.addAttribute("selectedElement", employeeDAO.getProductById(id));
        return "product/selectedElement";
    }
}
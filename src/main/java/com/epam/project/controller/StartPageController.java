package com.epam.project.controller;

import com.epam.project.dao.ProductDAO;
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

    private final ProductDAO productDAO;
    private final CallRepository callRepository;

    @Autowired
    public StartPageController(ProductDAO productDAO, CallRepository callRepository) {
        this.productDAO = productDAO;
        this.callRepository = callRepository;
    }

    @GetMapping()
    public String getStartPage() {
        return "main/mainPage";
    }

    @GetMapping("/aboutCompany")
    public String getAboutCompany() {
        return "aboutCompany/aboutCompany";
    }

    @GetMapping("/ourContacts")
    public String getOurContacts() {
        return "ourContacts/ourContacts";
    }

    @GetMapping("/callMe")
    public String getCallMe(@ModelAttribute("user") CallMe user) {
        return "callMe/callMe";
    }

    @GetMapping("/forbidden")
    public String getForbidden() {
        return "error/forbidden";
    }

    @PostMapping("/saveCall")
    private String saveCall(@ModelAttribute("user") @Valid CallMe callMe, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "callMe/callMe";
        }
        callRepository.save(callMe);
        return "redirect:/furniture";
    }

    //Посмотреть все имещиеся кровати
    @GetMapping("/showAll/{type}")
    public String getAllItem(@PathVariable("type") String type, Model model) {
        List<Product> products = productDAO.getAllItemsWithType(type);

        model.addAttribute("allItems", products);
        model.addAttribute("type", type);
        return "product/allAboutType";
    }

    //Получить данные о конкретном товаре
    @GetMapping("{id}")
    public String getSelectedElement(@PathVariable("id") int id, Model model) {
        model.addAttribute("selectedElement", productDAO.getProductById(id));
        return "product/selectedElement";
    }
}
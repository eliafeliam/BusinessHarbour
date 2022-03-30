package com.epam.project.controller;

import com.epam.project.model.UserEntity;
import com.epam.project.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Ten kontroler służy do zmiany istniejącego
// konta Użytkownika przez administratora.
@Controller
@RequestMapping("/admin")

public class AdminController {

    private final UserRepository userRepository;
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Lista wszystkich zarejestrowanych użytkowników
    @GetMapping()
    private String userList(Model model) {
        List<UserEntity> usersList = userRepository.findAll();
        model.addAttribute("users", usersList);
        return "admin/usersList";
    }
    //Znajdź użytkownika przez id do zmiany
    @GetMapping("/{id}")
    private String findPersonToEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("person", userRepository.findById(id).orElse(new UserEntity()));
        return "admin/editPerson";
    }
    //Pobieranie użytkownika i ustawianie nowych wartości
    @PatchMapping()
    private String editPerson(@ModelAttribute("person") UserEntity person) {
        userRepository.save(person);
        return "redirect:/admin";
    }
    //Usunięcie użytkownika z bazy danych
    @DeleteMapping("/{id}")
    private String delete(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }
}
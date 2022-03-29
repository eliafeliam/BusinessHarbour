package com.epam.project.controller;

import com.epam.project.model.UserEntity;
import com.epam.project.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")

public class AdminController {

    private final UserRepository userRepository;
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Список всех зарегестрированных пользователей
    @GetMapping()
    public String getAllUsers(Model model) {
        List<UserEntity> usersList = userRepository.findAll();
        model.addAttribute("users", usersList);
        return "admin/usersList";
    }
    //Получить пользователя по id для изменения
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("person", userRepository.findById(id).orElse(new UserEntity()));
        return "admin/editPerson";
    }
    //Получение пользователя и уставнока новых значений
    @PatchMapping()
    public String save(@ModelAttribute("person") UserEntity person) {
        userRepository.save(person);
        return "redirect:/admin";
    }
    //Удаление пользователя из БД
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }
}
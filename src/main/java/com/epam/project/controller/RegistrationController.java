package com.epam.project.controller;

import com.epam.project.model.Role;
import com.epam.project.model.Status;
import com.epam.project.model.UserEntity;
import com.epam.project.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;


import javax.validation.Valid;


@Controller
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository, JdbcTemplate jdbcTemplate,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user")UserEntity userEntity) {
        return "main/registration";
    }

    @PostMapping("/registration")
    public String userRegistration(@ModelAttribute("user") @Valid UserEntity userEntity,
                                   BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "main/registration";
        }
        if (!userEntity.getPassword().equals(userEntity.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "main/registration";
        }
        if (userRepository.findByEmail(userEntity.getEmail()).isPresent()){
            model.addAttribute("emailIsAlreadyExist",
                    "Пользователь с таким Email уже существует");
            return "main/registration";
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRole(Role.USER);
        userEntity.setStatus(Status.ACTIVE);
        userRepository.save(userEntity);
        return "redirect:/login";
    }
}

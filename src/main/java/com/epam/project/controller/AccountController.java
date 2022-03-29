package com.epam.project.controller;

import com.epam.project.model.OrderInfo;
import com.epam.project.model.UserEntity;
import com.epam.project.repository.OrderRepository;
import com.epam.project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {

    final
    UserRepository userRepository;
    final
    OrderRepository orderRepository;
    final
    PasswordEncoder passwordEncoder;

    public AccountController(UserRepository userRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public String allAboutUser (HttpServletRequest request, Model model) {
        Principal principal = request.getUserPrincipal();
        Optional<UserEntity> user = userRepository.findByEmail(principal.getName());
        model.addAttribute("user", user.orElse(new UserEntity("default")));

        Collection<OrderInfo> ordersList = orderRepository.findByEmail(user.orElse(new UserEntity("default")).getEmail());
        if(ordersList !=null) {
            model.addAttribute("ordersList", ordersList.toArray());
        }
        return "account/account";
    }
    @PatchMapping("/edit")
    String editUser(@ModelAttribute("user") @Valid UserEntity user, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "redirect:/account";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "redirect:/account";
        }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "redirect:/account";
    }
}

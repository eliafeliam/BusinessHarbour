package com.epam.project.controller;

import com.epam.project.model.CartNote;
import com.epam.project.model.Product;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

//Zawiera informacje o uwierzytelnieniu użytkownika
//w zależności od tego zmienia się widok strony
@ControllerAdvice
public class AuthenticationTagging {
    @ModelAttribute("isAuth")
    public String getAuth(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if(principal!=null) {
            return "authorized";
        } else {
            return  "notAuthorized";
        }
    }
}

package com.epam.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//Ten kontroler jest powiązany z Spring Security, za jego pomocą
// następuje przejście do strony w celu autoryzacji
@Controller
 class LoginController {
    @GetMapping("/login")
    public String getLoginPage() {
        return "login/login";
    }
}
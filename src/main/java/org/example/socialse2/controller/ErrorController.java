package org.example.socialse2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/access_denied")
    public String handleAccessDenied() {
        return "error/access_denied";
    }
}

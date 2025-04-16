package org.example.socialse2.controller;

import jakarta.validation.Valid;
import org.example.socialse2.dto.RegistrationDto;
import org.example.socialse2.model.User;
import org.example.socialse2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    private final UserService accountService;

    public AuthController(UserService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String displayRegistrationForm(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String processAccountRegistration(
            @Valid @ModelAttribute("registrationDto") RegistrationDto registrationDto,
            BindingResult bindingResult,
            Model model) {
            
        User existingAccount = accountService.retrieveUserByUsername(registrationDto.getUsername());
        if (existingAccount != null) {
            bindingResult.rejectValue("username", "username.exists", "This username is already taken");
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationDto", registrationDto);
            log.warn("Registration validation errors: {}", bindingResult.getAllErrors());
            return "register";
        }
        
        accountService.registerNewUser(registrationDto);
        log.info("New account registered successfully: {}", registrationDto.getUsername());
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String displayLoginForm() {
        return "login";
    }
    
    // Login processing is handled by Spring Security
}

package org.example.socialse2.controller;

import jakarta.validation.Valid;
import org.example.socialse2.dto.RegistrationDto;
import org.example.socialse2.model.User;
import org.example.socialse2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationDto") RegistrationDto registrationDto,
                               BindingResult bindingResult,
                               Model model) {
        User existingUser = userService.findByUsername(registrationDto.getUsername());
        if (existingUser != null) {
            bindingResult.rejectValue("username", "username.exists", "Username already exists");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationDto", registrationDto);
            return "register";
        }
        userService.createUser(registrationDto);
        return "redirect:/register?success";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    //
    //    @PostMapping("/login")
    //    public String loginUser(@Valid @ModelAttribute("loginDto") LoginDto loginDto,
    //                            BindingResult bindingResult,
    //                            Model model) {
    //        if (bindingResult.hasErrors()) {
    //            return "login";
    //        }
    //        if (!userService.authenticateUser(loginDto)) {
    //            model.addAttribute("loginError", "Invalid username or password");
    //            return "login";
    //        }
    //        return "redirect:/";
    //    }
}

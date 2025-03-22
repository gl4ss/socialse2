package org.example.socialse2.controller;

import jakarta.validation.Valid;
import org.example.socialse2.dto.UserDto;
import org.example.socialse2.mapper.UserMapper;
import org.example.socialse2.model.User;
import org.example.socialse2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showCurrentUserProfile(Principal principal) {
        if (principal == null) {
            return "error/access_denied";
        }
        org.example.socialse2.model.User currentUser = userService.getCurrentUser();
        return "redirect:/user/" + currentUser.getId();
    }

    @GetMapping("/{id}")
    public String showUserProfile(@PathVariable("id") Long id, Model model) {
        org.example.socialse2.model.User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/edit")
    public String showEditProfile(Model model, Principal principal) {
        if (principal == null) {
            return "error/access_denied";
        }
        User user = userService.getCurrentUser();
        UserDto userDto = UserMapper.toDto(user);
        model.addAttribute("user", userDto);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String updateProfile(@Valid @ModelAttribute("user") UserDto userDto,
                                BindingResult bindingResult,
                                Model model,
                                Principal principal) throws IOException {
        if (principal == null) {
            return "error/access_denied";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "user/edit";
        }
        User currentUser = userService.getCurrentUser();
        if (currentUser.getId().equals(userDto.getId())) {
            if (userDto.getImageFile() != null && !userDto.getImageFile().isEmpty()) {
                userDto.setProfileImage(userDto.getImageFile().getBytes());
                log.info("Updating profile image for {}", userDto.getImageFile());
            }
            userService.updateUser(userDto);
            log.info("User profile updated: {}", userDto);
        }
        return "redirect:/user/" + currentUser.getId();
    }

    @GetMapping("/{id}/delete")
    public String deleteProfile(Principal principal, @PathVariable Long id) {
        if (principal == null) {
            return "error/access_denied";
        }
        User currentUser = userService.getCurrentUser();
        if (userService.isAdmin(currentUser.getUsername())) {
            userService.deleteUser(id);
            return "redirect:/admin/users";
        }
        userService.deleteUser(id);
        return "redirect:/logout";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getUserProfileImage(@PathVariable Long id) {
        org.example.socialse2.model.User user = userService.findById(id);
        byte[] image = user.getProfileImage();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE).body(image);
    }

}

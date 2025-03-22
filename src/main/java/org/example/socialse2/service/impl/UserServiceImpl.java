package org.example.socialse2.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.socialse2.dto.RegistrationDto;
import org.example.socialse2.dto.UserDto;
import org.example.socialse2.mapper.UserMapper;
import org.example.socialse2.model.Post;
import org.example.socialse2.model.Role;
import org.example.socialse2.model.User;
import org.example.socialse2.repository.PostRepository;
import org.example.socialse2.repository.RoleRepository;
import org.example.socialse2.repository.UserRepository;
import org.example.socialse2.service.UserService;
import org.example.socialse2.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final PostRepository postRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           PostRepository postRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
    }

    @Transactional
    @Override
    public void createUser(RegistrationDto registrationDto) {
        org.example.socialse2.model.User user = new org.example.socialse2.model.User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Role defaultRole = roleRepository.findByName("ROLE_USER");
        if (defaultRole == null) {
            defaultRole = new Role();
            defaultRole.setName("ROLE_USER");
            roleRepository.save(defaultRole);
        }
        user.setRoles(Collections.singleton(defaultRole));
        userRepository.save(user);
    }

    @Override
    public org.example.socialse2.model.User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public void makeUserAdmin(String username) {
        org.example.socialse2.model.User user = userRepository.findByUsername(username);
        if (user != null) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
            user.getRoles().add(adminRole);
            userRepository.save(user);
        }
    }

    @Override
    public org.example.socialse2.model.User findById(Long id) {
        Optional<org.example.socialse2.model.User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    public org.example.socialse2.model.User getCurrentUser() {
        String username = Objects.requireNonNull(SecurityUtils.getCurrentUserDetails()).getUsername();
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateUser(UserDto user) {
        User existingUser = userRepository.findById(user.getId())
                                          .orElseThrow(() -> new EntityNotFoundException("User with id " +
                                                                                         user.getId() +
                                                                                         " not found"));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setProfileImage(user.getProfileImage());
        existingUser.setBio(user.getBio());
        existingUser.setWebsite(user.getWebsite());
        existingUser.setDateOfBirth(user.getDateOfBirth());
        if (user.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(existingUser);
    }

    @Override
    public boolean isAdmin(String username) {
        org.example.socialse2.model.User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_ADMIN"));
        } else {
            throw new EntityNotFoundException("User with username " + username + " not found");
        }
    }

    @Override
    public boolean isOwnerOfPost(String username, Long postId) {
        org.example.socialse2.model.User user = userRepository.findByUsername(username);
        if (user != null) {
            Post post = postRepository.findById(postId)
                                      .orElseThrow(() -> new EntityNotFoundException("Post with id " +
                                                                                     postId +
                                                                                     " not found"));
            return post.getUser().getUsername().equals(user.getUsername());
        } else {
            throw new EntityNotFoundException("User with username " + username + " not found");
        }
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User currentUser = getCurrentUser();
        User userToDelete = findById(id);
        if (currentUser.getId().equals(userToDelete.getId()) || isAdmin(currentUser.getUsername())) {
            // Remove the association between the User and the Role
            userToDelete.setRoles(null);
            // Save the User
            userRepository.save(userToDelete);
            // Now you can delete the User
            userRepository.delete(userToDelete);
        } else {
            throw new SecurityException("Unauthorized operation");
        }
    }

    @Override
    public List<UserDto> searchUsers(String query) {
        return userRepository.searchUsers(query).stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<UserDto> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable).map(UserMapper::toDto);
    }

}

package org.example.socialse2.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.example.socialse2.dto.RegistrationDto;
import org.example.socialse2.dto.UserDto;
import org.example.socialse2.mapper.UserMapper;
import org.example.socialse2.model.Post;
import org.example.socialse2.model.Role;
import org.example.socialse2.model.User;
import org.example.socialse2.repository.CommentRepository;
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

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public UserServiceImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            PostRepository postRepository,
            CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    @Override
    public void registerNewUser(RegistrationDto registrationDto) {
        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        Role defaultRole = getOrCreateRole(ROLE_USER);
        user.setRoles(Collections.singleton(defaultRole));

        userRepository.save(user);
    }

    @Override
    public User retrieveUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public void assignAdminRole(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Role adminRole = getOrCreateRole(ROLE_ADMIN);
            user.getRoles().add(adminRole);
            userRepository.save(user);
        }
    }

    private Role getOrCreateRole(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
        return role;
    }

    @Override
    public User retrieveUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with ID " + id + " not found in system"));
    }

    @Override
    public User retrieveCurrentAuthenticatedUser() {
        String username = Objects.requireNonNull(SecurityUtils.getCurrentUserDetails(), "Authentication details unavailable").getUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("Currently authenticated user not found in database");
        }
        return user;
    }

    @Override
    public void modifyUserProfile(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Account with ID " + userDto.getId() + " not found in system"));

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setUsername(userDto.getUsername());
        existingUser.setProfileImage(userDto.getProfileImage());
        existingUser.setBio(userDto.getBio());
        existingUser.setWebsite(userDto.getWebsite());
        existingUser.setDateOfBirth(userDto.getDateOfBirth());

        if (userDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userRepository.save(existingUser);
    }

    @Override
    public boolean hasAdminPrivileges(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User with username " + username + " not found in system");
        }

        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(ROLE_ADMIN));
    }

    @Override
    public boolean isPostCreator(String username, Long postId) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User with username " + username + " not found in system");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Content with ID " + postId + " not found in system"));

        return post.getUser().getUsername().equals(username);
    }

    @Transactional
    @Override
    public void removeUserAccount(Long id) {
        User currentUser = retrieveCurrentAuthenticatedUser();
        User userToDelete = retrieveUserById(id);

        if (currentUser.getId().equals(userToDelete.getId()) || hasAdminPrivileges(currentUser.getUsername())) {
            userToDelete.getRoles().clear();
            userRepository.save(userToDelete);

            commentRepository.deleteByUserId(userToDelete.getId());

            if (userToDelete.getPosts() != null && !userToDelete.getPosts().isEmpty()) {
                List<Long> postIds = userToDelete.getPosts().stream()
                        .map(Post::getId)
                        .collect(Collectors.toList());

                for (Long postId : postIds) {
                    postRepository.findById(postId).ifPresent(postRepository::delete);
                }
            }

            userRepository.delete(userToDelete);
        } else {
            throw new SecurityException("Access denied: insufficient privileges for account deletion");
        }
    }

    @Override
    public List<UserDto> findUsersBySearchTerm(String query) {
        return userRepository.searchUsers(query).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDto> retrieveUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable).map(UserMapper::toDto);
    }
}

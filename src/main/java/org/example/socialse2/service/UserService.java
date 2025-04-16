package org.example.socialse2.service;

import org.example.socialse2.dto.RegistrationDto;
import org.example.socialse2.dto.UserDto;
import org.example.socialse2.model.User;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    // Renamed to better reflect method purpose
    void registerNewUser(RegistrationDto registrationDto);

    User retrieveUserByUsername(String username);

    void assignAdminRole(String username);

    User retrieveUserById(Long id);

    User retrieveCurrentAuthenticatedUser();

    void modifyUserProfile(UserDto user);

    boolean hasAdminPrivileges(String username);

    boolean isPostCreator(String username, Long postId);

    @Transactional
    void removeUserAccount(Long id);

    List<UserDto> findUsersBySearchTerm(String query);

    Page<UserDto> retrieveUsersPaginated(int page, int size);
}

package org.example.socialse2.mapper;

import org.example.socialse2.dto.UserDto;
import org.example.socialse2.model.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setProfileImage(user.getProfileImage());
        userDto.setBio(user.getBio());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setWebsite(user.getWebsite());
        return userDto;
    }

}

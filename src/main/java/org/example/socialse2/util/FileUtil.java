package org.example.socialse2.util;

import org.example.socialse2.dto.PostDto;
import org.example.socialse2.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Utility class for handling file uploads
 */
public class FileUtil {
    
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    
    /**
     * Process image upload for user profile
     * 
     * @param userDto The user DTO containing the image file
     * @throws IOException If there's an error processing the image
     */
    public static void processImageUpload(UserDto userDto) throws IOException {
        if (userDto.getImageFile() != null && !userDto.getImageFile().isEmpty()) {
            userDto.setProfileImage(userDto.getImageFile().getBytes());
            log.info("Processing profile image: {}", userDto.getImageFile().getOriginalFilename());
        }
    }
    
    /**
     * Process image upload for post
     * 
     * @param postDto The post DTO containing the image file
     * @throws IOException If there's an error processing the image
     */
    public static void processPostImage(PostDto postDto) throws IOException {
        if (postDto.getImageFile() != null && !postDto.getImageFile().isEmpty()) {
            postDto.setImage(postDto.getImageFile().getBytes());
            log.info("Processing post image: {}", postDto.getImageFile().getOriginalFilename());
        }
    }
}

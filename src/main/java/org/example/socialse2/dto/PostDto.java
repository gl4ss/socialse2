package org.example.socialse2.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;

    @NotEmpty(message = "Title must not be empty")
    private String title;

    @NotEmpty(message = "Content must not be empty")
    @Length(message = "Content must be less than 65535 characters", max = 65535)
    private String content;

    @NotEmpty(message = "Short description must not be empty")
    private String shortDescription;

    private byte[] image;

    private MultipartFile imageFile;

    private Long upVotes;

    private Long downVotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String ownerName;

    private Long ownerId;

}

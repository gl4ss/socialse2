package org.example.socialse2.repository;

import org.example.socialse2.model.Comment;
import org.example.socialse2.model.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findByPostId(Long postId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.user.id = :userId")
    void deleteByUserId(Long userId);
}

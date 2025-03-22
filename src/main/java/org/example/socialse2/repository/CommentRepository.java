package org.example.socialse2.repository;

import org.example.socialse2.model.Comment;
import org.example.socialse2.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findByPostId(Long postId);

}

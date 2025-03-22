package org.example.socialse2.repository;

import jakarta.transaction.Transactional;
import org.example.socialse2.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE " +
           "p.title LIKE CONCAT('%', :query,'%') OR " +
           "p.shortDescription LIKE CONCAT('%', :query,'%')")
    List<Post> searchPosts(String query);

}
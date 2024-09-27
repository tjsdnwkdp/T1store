package com.T1store.T1store.repository;

import com.T1store.T1store.dto.PostSearchDto;
import com.T1store.T1store.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p " +
            "WHERE (:#{#postSearchDto.searchQuery} IS NULL OR p.title LIKE %:#{#postSearchDto.searchQuery}%) " +
            "AND (:#{#postSearchDto.searchBy} IS NULL OR p.author.name LIKE %:#{#postSearchDto.searchBy}%)")
    Page<Post> findPosts(@Param("postSearchDto") PostSearchDto postSearchDto, Pageable pageable);
}

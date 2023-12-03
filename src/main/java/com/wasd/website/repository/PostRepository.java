package com.wasd.website.repository;

import com.wasd.website.entity.Post;
import com.wasd.website.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PostRepository extends JpaRepository<Post, Long> {
    Collection<Post> findByAuthor(User user);
}

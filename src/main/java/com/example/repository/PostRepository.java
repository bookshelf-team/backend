package com.example.repository;

import com.example.model.Post;
import com.example.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @NotNull
    List<Post> findAll();

    List<Post> findAllByAddedByUser(User user);

    @NotNull
    Optional<Post> findById(@NotNull Long id);

    boolean existsById(@NotNull Long id);

    void deleteById(@NotNull Long id);
}

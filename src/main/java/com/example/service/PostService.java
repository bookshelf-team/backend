package com.example.service;

import com.example.exception.CustomAccessDeniedException;
import com.example.model.Post;
import com.example.model.User;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getAllPostsByUsername(String username) {
        return postRepository.findAllByAddedByUser(userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User is not exist")));
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post is not exist"));
    }

    @Transactional
    public String addPost(Post post) {
        Post postToSave = new Post(post.getTitle(), post.getBody());
        postToSave.setAddedByUser(getUserFromSecurityContext(SecurityContextHolder.getContext()));
        postToSave.setPublicationDate(getTimeStamp());
        postRepository.save(postToSave);
        return "Post added successfully";
    }

    @Transactional
    public Post updatePostById(Long id, Post post) {
        Post sevedPost = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post is not exist"));

        if (!sevedPost.getAddedByUser().equals(getUserFromSecurityContext(SecurityContextHolder.getContext()))
                && getUserRoles(SecurityContextHolder.getContext()).contains("USER")) {
            throw new CustomAccessDeniedException("User doesn't have access to update this post");
        }

        sevedPost.setTitle(post.getTitle());
        sevedPost.setBody(post.getBody());
        sevedPost.setImageUrl(post.getImageUrl());
        sevedPost.setLastChangeDate(getTimeStamp());
        return postRepository.save(sevedPost);
    }

    @Transactional
    public String deletePostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post is not exist"));

        if (!post.getAddedByUser().equals(getUserFromSecurityContext(SecurityContextHolder.getContext()))
                && getUserRoles(SecurityContextHolder.getContext()).contains("ROLE_USER")) {
            throw new CustomAccessDeniedException("User doesn't have access to update this post");
        }

        postRepository.deleteById(id);
        return "Post deleted successfully";
    }

    private User getUserFromSecurityContext(SecurityContext context) {
        return userRepository.findByUsername(context.getAuthentication().getName())
                .orElseThrow(() -> new NoSuchElementException("User from token is not exist"));
    }

    public List<String> getUserRoles(SecurityContext context) {
        return context.getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    private String getTimeStamp() {
        return dateTimeFormat.format(new Date());
    }
}

package com.example.controller;

import com.example.model.Post;
import com.example.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/all")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/username")
    public List<Post> getAllPostsByUsername(@RequestParam String username) {
        return postService.getAllPostsByUsername(username);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/add")
    public String addPost(@RequestBody Post post) {
        return postService.addPost(post);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/id")
    public Post updatePostById(@RequestParam Long id, @RequestBody Post post) {
        return postService.updatePostById(id, post);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping("/delete/id")
    public String deletePostById(@RequestParam Long id) {
        return postService.deletePostById(id);
    }
}

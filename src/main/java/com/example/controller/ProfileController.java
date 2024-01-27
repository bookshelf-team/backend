package com.example.controller;

import com.example.model.Profile;
import com.example.payload.request.BookToProfileRelationRequest;
import com.example.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    public Profile getProfileByUsername(@PathVariable String username) {
        return profileService.getProfileByUsername(username);
    }

    @PreAuthorize("(#username == authentication.name and hasRole('USER')) or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/{username}")
    public Profile updateProfileByUsername(@PathVariable String username, @RequestBody Profile profileRequest) {
        return profileService.updateProfileByUsername(username, profileRequest);
    }

    @PreAuthorize("(#bookToProfileRelationRequest.username == authentication.name and hasRole('USER')) or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping
    public String addBookToProfile(@RequestBody BookToProfileRelationRequest bookToProfileRelationRequest) {
        return profileService.addBookToProfile(bookToProfileRelationRequest);
    }
}

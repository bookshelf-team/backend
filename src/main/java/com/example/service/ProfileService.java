package com.example.service;

import com.example.model.Book;
import com.example.model.BookRelationType;
import com.example.model.Profile;
import com.example.model.User;
import com.example.model.ProfileBookRelation;
import com.example.repository.UserRepository;
import com.example.repository.BookRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.ProfileBookRelationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final BookRepository bookRepository;

    private final ProfileBookRelationRepository profileBookRelationRepository;

    @Transactional
    public Profile updateProfile(String username, Profile updatedProfile) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            Profile profile = optionalUser.get().getProfile();
            BeanUtils.copyProperties(updatedProfile, profile, "id", "profileBookRelations");
            return profileRepository.save(profile);
        } else {
            throw new NoSuchElementException("User is not exist");
        }
    }

    @Transactional
    public void addBookToProfile(String username, Long bookId, BookRelationType relationType) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            Profile profile = optionalUser.get().getProfile();
            Optional<Book> optionalBook = bookRepository.findById(bookId);
            if (optionalBook.isPresent()) {
                ProfileBookRelation relation = new ProfileBookRelation(profile, optionalBook.get(), relationType);
                profileBookRelationRepository.save(relation);
            } else {
                throw new NoSuchElementException("Book is not exist");
            }
        } else {
            throw new NoSuchElementException("User is not exist");
        }
    }
}

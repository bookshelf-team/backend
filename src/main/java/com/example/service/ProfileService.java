package com.example.service;

import com.example.exception.ConflictException;
import com.example.model.Book;
import com.example.model.Profile;
import com.example.model.User;
import com.example.model.ProfileBookRelation;
import com.example.model.EBookRelationType;
import com.example.payload.request.BookToProfileRelationRequest;
import com.example.repository.UserRepository;
import com.example.repository.BookRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.ProfileBookRelationRepository;
import com.example.repository.BookRelationTypeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final BookRepository bookRepository;

    private final ProfileBookRelationRepository profileBookRelationRepository;

    private final BookRelationTypeRepository bookRelationTypeRepository;

    private final Validator validator;

    public Profile getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User is not exist"));
        return user.getProfile();
    }

    @Transactional
    public Profile updateProfileByUsername(String username, Profile updatedProfile) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User is not exist"));

        Profile profile = user.getProfile();

        BeanUtils.copyProperties(updatedProfile, profile, "id", "profileBookRelations");
        return profileRepository.save(profile);
    }

    @Transactional
    public String addBookToProfile(BookToProfileRelationRequest bookToProfileRelationRequest) {
        Profile profile = getProfileFromBookToProfileRelationRequest(bookToProfileRelationRequest);

        Book book = bookRepository.findByIsbn((bookToProfileRelationRequest.getBookIsbn()))
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        if (profileBookRelationRepository.existsByProfileAndBookIsbn(profile, book.getIsbn())) {
            throw new ConflictException("This book to profile relation is already taken");
        }

        ProfileBookRelation relation = new ProfileBookRelation(profile, book.getIsbn(),
                bookRelationTypeRepository.findByName(EBookRelationType.valueOf("TYPE_"
                                + bookToProfileRelationRequest.getRelationType().toUpperCase()))
                        .orElseThrow(() -> new NoSuchElementException("Book relation type is not exist")));
        profileBookRelationRepository.save(relation);
        return "Book added to profile successfully";
    }

    @Transactional
    public String changeBookFromProfileRelationType(BookToProfileRelationRequest bookToProfileRelationRequest) {
        Profile profile = getProfileFromBookToProfileRelationRequest(bookToProfileRelationRequest);

        Book book = bookRepository.findByIsbn((bookToProfileRelationRequest.getBookIsbn()))
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        ProfileBookRelation profileBookRelation = profileBookRelationRepository
                .findByProfileAndBookIsbn(profile, book.getIsbn())
                .orElseThrow(() -> new NoSuchElementException("This book to profile relation is not exist"));

        profileBookRelation.setRelationType(bookRelationTypeRepository
                .findByName(EBookRelationType.valueOf("TYPE_"
                        + bookToProfileRelationRequest.getRelationType().toUpperCase()))
                .orElseThrow(() -> new NoSuchElementException("Book relation type is not exist")));

        return "Book to profile relation updated successfully";
    }

    @Transactional
    public String deleteBookFromProfile(BookToProfileRelationRequest bookToProfileRelationRequest) {
        Profile profile = getProfileFromBookToProfileRelationRequest(bookToProfileRelationRequest);

        Book book = bookRepository.findByIsbn((bookToProfileRelationRequest.getBookIsbn()))
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        ProfileBookRelation profileBookRelation = profileBookRelationRepository
                .findByProfileAndBookIsbn(profile, book.getIsbn())
                .orElseThrow(() -> new NoSuchElementException("This book to profile relation is not exist"));

        profileBookRelationRepository.deleteById(profileBookRelation.getId());

        return "Book deleted from profile successfully";
    }

    private Profile getProfileFromBookToProfileRelationRequest(
            BookToProfileRelationRequest bookToProfileRelationRequest) {
        Set<ConstraintViolation<BookToProfileRelationRequest>> violations =
                validator.validate(bookToProfileRelationRequest);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));
        }

        User user = userRepository.findByUsername(bookToProfileRelationRequest.getUsername())
                .orElseThrow(() -> new NoSuchElementException("User is not exist"));

        return user.getProfile();
    }
}

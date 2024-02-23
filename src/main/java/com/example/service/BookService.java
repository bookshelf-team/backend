package com.example.service;

import com.example.exception.ConflictException;
import com.example.exception.CustomAccessDeniedException;
import com.example.model.*;
import com.example.payload.request.BookRequest;
import com.example.repository.BookRepository;
import com.example.repository.GenreRepository;
import com.example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@AllArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final UserRepository userRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));
    }

    public List<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbnContainingIgnoreCase(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));
    }

    public List<Book> getBooksByGenre(String genreName) {
        return bookRepository.findByGenre(genreRepository.findByName(EGenre.valueOf("GENRE_" + genreName.toUpperCase()))
                .orElseThrow(() -> new NoSuchElementException("Genre is not found: "
                        + "GENRE_" + genreName.toUpperCase())));
    }

    public List<Book> getBooksByGenres(List<String> genreNames) {
        List<Genre> genres = new ArrayList<>();
        for (String genreName: genreNames) {
            genres.add(genreRepository.findByName(EGenre.valueOf("GENRE_" + genreName.toUpperCase()))
                    .orElseThrow(() -> new NoSuchElementException("Genre is not found: "
                            + "GENRE_" + genreName.toUpperCase())));
        }

        return bookRepository.findByGenres(genres, (long) genres.size());
    }

    @Transactional
    public String addBook(BookRequest bookRequest) {
        if (bookRepository.existsByIsbn(bookRequest.getIsbn())) {
            throw new ConflictException("ISBN is already taken");
        }

        Book bookToSave = new Book();
        copyBookProperties(bookRequest, bookToSave);
        bookToSave.setAddedByUser(getUserFromSecurityContext(SecurityContextHolder.getContext()));
        bookRepository.save(bookToSave);
        return "Book added successfully";
    }

    @Transactional
    public Book updateBookById(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        return validateUserPermissions(bookRequest, book);
    }

    @Transactional
    public Book updateBookByIsbn(String isbn, BookRequest bookRequest) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        return validateUserPermissions(bookRequest, book);
    }

    @Transactional
    public String deleteBookById(Long id) {
        if (!bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist")).getAddedByUser()
                .equals(getUserFromSecurityContext(SecurityContextHolder.getContext()))
                && getUserRoles(SecurityContextHolder.getContext()).contains("ROLE_USER")) {
            throw new CustomAccessDeniedException("User doesn't have access to update this book");
        }

        bookRepository.deleteById(id);
        return "Book deleted successfully";
    }

    @Transactional
    public String deleteBookByIsbn(String isbn) {
        if (!bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist")).getAddedByUser()
                .equals(getUserFromSecurityContext(SecurityContextHolder.getContext()))
                && getUserRoles(SecurityContextHolder.getContext()).contains("USER")) {
            throw new CustomAccessDeniedException("User doesn't have access to update this book");
        }

        bookRepository.deleteByIsbn(isbn);
        return "Book deleted successfully";
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

    private void copyBookProperties(BookRequest bookRequest, Book book) {
        BeanUtils.copyProperties(bookRequest, book, "id", "genres", "profileBookRelations");

        Set<Genre> genres = new HashSet<>();
        for (String genreName: bookRequest.getGenres()) {
            genres.add(genreRepository.findByName(EGenre.valueOf("GENRE_" + genreName.toUpperCase()))
                    .orElseThrow(() -> new NoSuchElementException("Genre is not found: "
                            + "GENRE_" + genreName.toUpperCase())));
        }

        book.setGenres(genres);
    }

    private Book validateUserPermissions(BookRequest bookRequest, Book book) {
        if (!book.getAddedByUser().equals(getUserFromSecurityContext(SecurityContextHolder.getContext()))
                && getUserRoles(SecurityContextHolder.getContext()).contains("USER")) {
            throw new CustomAccessDeniedException("User doesn't have access to update this book");
        }

        if (!book.getIsbn().equals(bookRequest.getIsbn()) && bookRepository.existsByIsbn(bookRequest.getIsbn())) {
            throw new ConflictException("ISBN is already taken");
        }

        copyBookProperties(bookRequest, book);
        return bookRepository.save(book);
    }
}

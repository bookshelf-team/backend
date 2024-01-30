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

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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

    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));
    }

    public List<Book> getBooksByGenre(String genreName) {
        Genre genre = genreRepository.findByName(EGenre.valueOf("GENRE_" + genreName.toUpperCase()))
                .orElseThrow(() -> new NoSuchElementException("Genre is not found: " +
                        "GENRE_" + genreName.toUpperCase()));

        return bookRepository.findByGenre(genre);
    }

    @Transactional
    public String addBook(BookRequest bookRequest) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NoSuchElementException("User from token is not exist"));

        if (bookRepository.existsByIsbn(bookRequest.getIsbn())) {
            throw new ConflictException("ISBN is already taken");
        }

        Book bookToSave = new Book();
        copyBookProperties(bookRequest, bookToSave);
        bookToSave.setAddedByUser(user);
        bookRepository.save(bookToSave);
        return "Book added successfully";
    }

    @Transactional
    public Book updateBookById(Long id, BookRequest bookRequest) {
        User user = getUserFromSecurityContext(SecurityContextHolder.getContext());

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        if (!book.getAddedByUser().equals(user) && getUserRoles(SecurityContextHolder.getContext()).contains("USER")) {
            throw new CustomAccessDeniedException("User doesn't have access to update this book");
        }

        if (!book.getIsbn().equals(bookRequest.getIsbn()) && bookRepository.existsByIsbn(bookRequest.getIsbn())) {
            throw new ConflictException("ISBN is already taken");
        }

        copyBookProperties(bookRequest, book);
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBookByIsbn(String isbn, BookRequest bookRequest) {
        User user = getUserFromSecurityContext(SecurityContextHolder.getContext());

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        if (!book.getAddedByUser().equals(user) && getUserRoles(SecurityContextHolder.getContext()).contains("USER")) {
            throw new CustomAccessDeniedException("User doesn't have access to update this book");
        }

        if (!book.getIsbn().equals(bookRequest.getIsbn()) && bookRepository.existsByIsbn(bookRequest.getIsbn())) {
            throw new ConflictException("ISBN is already taken");
        }

        copyBookProperties(bookRequest, book);
        return bookRepository.save(book);
    }

    @Transactional
    public String deleteBookById(Long id) {
        User user = getUserFromSecurityContext(SecurityContextHolder.getContext());

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        if (!book.getAddedByUser().equals(user) && getUserRoles(SecurityContextHolder.getContext()).contains("ROLE_USER")) {
            throw new CustomAccessDeniedException("User doesn't have access to update this book");
        }

        bookRepository.deleteById(id);
        return "Book deleted successfully";
    }

    @Transactional
    public String deleteBookByIsbn(String isbn) {
        User user = getUserFromSecurityContext(SecurityContextHolder.getContext());

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        if (!book.getAddedByUser().equals(user) && getUserRoles(SecurityContextHolder.getContext()).contains("USER")) {
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
                    .orElseThrow(() -> new NoSuchElementException("Genre is not found: " +
                            "GENRE_" + genreName.toUpperCase())));
        }

        book.setGenres(genres);
    }
}

package com.example.service;

import com.example.exception.ConflictException;
import com.example.model.*;
import com.example.payload.request.BookRequest;
import com.example.repository.BookRepository;
import com.example.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
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
        if (bookRepository.existsByIsbn(bookRequest.getIsbn())) {
            throw new ConflictException("ISBN is already taken");
        }

        Book bookToSave = new Book();
        copyBookProperties(bookRequest, bookToSave);
        bookRepository.save(bookToSave);
        return "Book added successfully";
    }

    @Transactional
    public Book updateBookById(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        if (!book.getIsbn().equals(bookRequest.getIsbn()) && bookRepository.existsByIsbn(bookRequest.getIsbn())) {
            throw new ConflictException("ISBN is already taken");
        }

        copyBookProperties(bookRequest, book);
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBookByIsbn(String isbn, BookRequest bookRequest) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NoSuchElementException("Book is not exist"));

        if (!book.getIsbn().equals(bookRequest.getIsbn()) && bookRepository.existsByIsbn(bookRequest.getIsbn())) {
            throw new ConflictException("ISBN is already taken");
        }

        copyBookProperties(bookRequest, book);
        return bookRepository.save(book);
    }

    @Transactional
    public String deleteBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NoSuchElementException("Book is not exist");
        }

        bookRepository.deleteById(id);
        return "Book deleted successfully";
    }

    @Transactional
    public String deleteBookByIsbn(String isbn) {
        if (!bookRepository.existsByIsbn(isbn)) {
            throw new NoSuchElementException("Book is not exist");
        }

        bookRepository.deleteByIsbn(isbn);
        return "Book deleted successfully";
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

package com.example.service;

import com.example.model.Book;
import com.example.model.EGenre;
import com.example.model.Genre;
import com.example.repository.BookRepository;
import com.example.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
                .orElseThrow(() -> new NoSuchElementException("Genre is not found: " + genreName));

        return bookRepository.findByGenre(genre);
    }
}

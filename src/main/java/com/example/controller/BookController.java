package com.example.controller;

import com.example.model.Book;
import com.example.payload.request.BookRequest;
import com.example.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/book")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/search/isbn")
    public List<Book> getBookByIsbn(@RequestParam String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @GetMapping("/search/title")
    public List<Book> getBooksByTitle(@RequestParam String title) {
        return bookService.getBooksByTitle(title);
    }

    @GetMapping("/search/author")
    public List<Book> getBooksByAuthor(@RequestParam String author) {
        return bookService.getBooksByAuthor(author);
    }

    @GetMapping("/search/genre")
    public List<Book> getBooksByGenre(@RequestParam String genre) {
        return bookService.getBooksByGenre(genre);
    }

    @GetMapping("/search/genres")
    public List<Book> getBooksByGenre(@RequestParam List<String> genres) {
        return bookService.getBooksByGenres(genres);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/add")
    public String addBook(@RequestBody BookRequest bookRequest) {
        return bookService.addBook(bookRequest);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/id")
    public Book updateBookById(@RequestParam Long id, @RequestBody BookRequest bookRequest) {
        return bookService.updateBookById(id, bookRequest);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/isbn")
    public Book updateBookByIsbn(@RequestParam String isbn, @RequestBody BookRequest bookRequest) {
        return bookService.updateBookByIsbn(isbn, bookRequest);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping("/delete/id")
    public String deleteBookById(@RequestParam Long id) {
        return bookService.deleteBookById(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @DeleteMapping("/delete/isbn")
    public String deleteBookByIsbn(@RequestParam String isbn) {
        return bookService.deleteBookByIsbn(isbn);
    }
}

package com.example.controller;

import com.example.model.Book;
import com.example.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/book")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return null;
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/search/title")
    public List<Book> getBooksByTitle(@RequestParam String title) {
        return null;
    }

    @GetMapping("/search/author")
    public List<Book> getBooksByAuthor(@RequestParam String author) {
        return null;
    }
}

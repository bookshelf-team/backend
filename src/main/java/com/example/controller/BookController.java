package com.example.controller;

import com.example.model.Book;
import com.example.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/book")
@AllArgsConstructor

public class BookController {

    public BookController(BookService bookService) {
    }

    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> getBooksByTitle(@RequestParam String title) {
        return null;
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> getBooksByAuthor(@RequestParam String author) {
        return null;
    }
}
package com.example.service;

import com.example.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    public BookService(BookRepository bookRepository) {

    }
}

package com.example.repository;

import com.example.model.Book;
import com.example.model.Genre;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @NotNull
    List<Book> findAll();

    @NotNull
    Optional<Book> findById(@NotNull Long id);

    Optional<Book> findByIsbn(String isbn);

    Optional<List<Book>> findByTitle(String title);

    Optional<List<Book>> findByAuthor(String author);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.genre g WHERE g = :genre")
    List<Book> findByGenre(Genre genre);
}

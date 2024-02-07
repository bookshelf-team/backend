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

    @Query("SELECT b FROM Book b WHERE lower(b.isbn) LIKE lower(concat('%', :isbn, '%')) "
            + "ORDER BY FUNCTION('levenshtein', b.isbn, :isbn)")
    Optional<List<Book>> findByIsbnContainingIgnoreCase(String isbn);

    @Query("SELECT b FROM Book b WHERE lower(b.title) LIKE lower(concat('%', :title, '%')) "
            + "ORDER BY FUNCTION('levenshtein', b.title, :title)")
    Optional<List<Book>> findByTitleContainingIgnoreCase(@NotNull String title);

    @Query("SELECT b FROM Book b WHERE lower(b.author) LIKE lower(concat('%', :author, '%')) "
            + "ORDER BY FUNCTION('levenshtein', b.author, :author)")
    Optional<List<Book>> findByAuthorContainingIgnoreCase(@NotNull String author);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.genres g WHERE g = :genre")
    List<Book> findByGenre(Genre genre);

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g IN :genres GROUP BY b HAVING COUNT(DISTINCT g) = :genresCount")
    List<Book> findByGenres(@NotNull List<Genre> genres, @NotNull Long genresCount);

    boolean existsById(@NotNull Long id);

    void deleteById(@NotNull Long id);

    boolean existsByIsbn(String isbn);

    void deleteByIsbn(@NotNull String sbn);
}

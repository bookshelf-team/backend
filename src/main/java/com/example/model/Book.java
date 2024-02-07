package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    @NotBlank(message = "Author cannot be blank")
    @Size(max = 100)
    private String author;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000)
    private String description;

    @NotNull(message = "Publication year cannot be null")
    @Max(2024)
    private Integer publicationYear;

    @NotBlank(message = "ISBN cannot be blank")
    @Size(max = 20)
    private String isbn;

    @NotNull(message = "Page count cannot be null")
    private Integer pageCount;

    private String coverImageUrl;

    private String diskImageUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "added_by_user_id")
    private User addedByUser;
}

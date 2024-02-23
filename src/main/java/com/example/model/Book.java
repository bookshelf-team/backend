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

    @NotBlank
    @Size(max = 100)
    private String author;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    @Max(2024)
    private Integer publicationYear;

    @NotBlank
    @Size(max = 20)
    private String isbn;

    @NotNull
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

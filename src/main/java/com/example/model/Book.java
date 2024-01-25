package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book extends BaseEntity {

    private String author;

    private String title;

    private String description;

    private int  publicationYear;

    private String isbn;

    private String genre;

    private int pageCount;

    private String coverImageUrl;

    private String diskImageUrl;
}


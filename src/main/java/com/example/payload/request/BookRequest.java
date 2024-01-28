package com.example.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookRequest {

    @NotBlank(message = "Author cannot be blank")
    @Size(max = 100)
    String author;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100)
    String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000)
    String description;

    @NotNull(message = "Publication year cannot be blank")
    @Max(2024)
    Integer publicationYear;

    @NotBlank(message = "ISBN cannot be blank")
    @Size(max = 20)
    String isbn;

    @NotNull(message = "Page count cannot be blank")
    Integer pageCount;

    String coverImageUrl;

    String diskImageUrl;

    @NotEmpty(message = "Genres cannot be blank")
    Set<String> genres;
}

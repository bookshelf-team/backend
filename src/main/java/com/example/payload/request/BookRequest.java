package com.example.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookRequest {

    @NotBlank
    @Size(max = 100)
    String author;

    @NotBlank
    @Size(max = 100)
    String title;

    @NotBlank
    @Size(max = 1000)
    String description;

    @NotNull
    @Max(2024)
    Integer publicationYear;

    @NotBlank
    @Size(max = 20)
    String isbn;

    @NotNull
    Integer pageCount;

    String coverImageUrl;

    String diskImageUrl;

    @NotEmpty
    Set<String> genres;
}

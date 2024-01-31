package com.example.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookToProfileRelationRequest {
    @NotBlank(message = "Username cannot be blank")
    String username;

    @NotNull(message = "Book ISBN cannot be null")
    String bookIsbn;

    @NotBlank(message = "Relation Type cannot be blank")
    String relationType;
}

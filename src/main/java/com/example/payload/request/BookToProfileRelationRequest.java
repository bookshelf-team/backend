package com.example.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookToProfileRelationRequest {
    @NotBlank(message = "Username cannot be blank")
    String username;

    @NotBlank(message = "Book ID cannot be blank")
    Long bookId;

    @NotBlank(message = "Relation Type cannot be blank")
    String relationType;
}

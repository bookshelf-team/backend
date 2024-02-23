package com.example.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookToProfileRelationRequest {
    @NotBlank
    String username;

    @NotNull
    String bookIsbn;

    @NotBlank
    String relationType;
}

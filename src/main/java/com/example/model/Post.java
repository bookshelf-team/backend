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
@Table(name = "posts")
public class Post extends BaseEntity {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "Body cannot be blank")
    @Size(max = 1000)
    private String body;

    @Size(max = 120)
    private String publicationDate;

    @Size(max = 120)
    private String lastChangeDate;

    private String imageUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "added_by_user_id")
    private User addedByUser;

    public Post(String title, String body) {
        this.title = title;
        this.body = body;
    }
}

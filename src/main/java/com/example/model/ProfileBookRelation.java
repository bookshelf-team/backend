package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile_books")
public class ProfileBookRelation extends BaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private String bookIsbn;

    @ManyToOne
    @JoinColumn(name = "relation_type_id")
    private BookRelationType relationType;
}

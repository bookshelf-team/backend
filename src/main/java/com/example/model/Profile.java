package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity {

    private String firstName;

    private String lastName;

    private String gender;

    private String birthday;

    private String phone;

    private String address;

    private String avatar;

    private String language;

    private String about;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfileBookRelation> profileBookRelations = new HashSet<>();
}

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
}

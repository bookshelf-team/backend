package com.example.service;

import com.example.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

}

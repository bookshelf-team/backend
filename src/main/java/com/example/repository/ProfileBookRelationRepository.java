package com.example.repository;

import com.example.model.ProfileBookRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileBookRelationRepository extends JpaRepository<ProfileBookRelation, Long> {

}

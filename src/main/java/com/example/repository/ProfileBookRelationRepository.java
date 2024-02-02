package com.example.repository;

import com.example.model.Book;
import com.example.model.Profile;
import com.example.model.ProfileBookRelation;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileBookRelationRepository extends JpaRepository<ProfileBookRelation, Long> {

    boolean existsByProfileAndBook(Profile profile, Book book);

    Optional<ProfileBookRelation> findByProfileAndBook(Profile profile, Book book);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProfileBookRelation pb WHERE pb.id = :id")
    void deleteById(@NotNull Long id);
}

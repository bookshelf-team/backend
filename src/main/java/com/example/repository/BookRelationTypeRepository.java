package com.example.repository;

import com.example.model.EBookRelationType;
import com.example.model.BookRelationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRelationTypeRepository extends JpaRepository<BookRelationType, Long> {
    Optional<BookRelationType> findByName(EBookRelationType name);
}

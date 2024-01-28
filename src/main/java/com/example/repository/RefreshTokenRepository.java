package com.example.repository;

import com.example.model.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Transactional
    @Modifying
    @Query("DELETE FROM refresh_token rt WHERE rt.user.id = :userId")
    void deleteByUserId(Long userId);
}

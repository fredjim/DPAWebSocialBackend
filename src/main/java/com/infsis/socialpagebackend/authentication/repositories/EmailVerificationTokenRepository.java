package com.infsis.socialpagebackend.authentication.repositories;

import com.infsis.socialpagebackend.authentication.models.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Integer> {
    Optional<EmailVerificationToken> findByToken(String token);
}

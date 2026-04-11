package com.infsis.socialpagebackend.moderation.repositories;

import com.infsis.socialpagebackend.moderation.models.BlacklistWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistWordRepository extends JpaRepository<BlacklistWord, Long> {

    List<BlacklistWord> findAllByActiveTrue();

    List<BlacklistWord> findAllByExcludeFromFileTrue();

    Optional<BlacklistWord> findByWordIgnoreCase(String word);

    boolean existsByWordIgnoreCase(String word);
}

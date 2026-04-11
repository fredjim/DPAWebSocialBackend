package com.infsis.socialpagebackend.moderation.repositories;

import com.infsis.socialpagebackend.moderation.enums.ContentType;
import com.infsis.socialpagebackend.moderation.enums.ModerationDecision;
import com.infsis.socialpagebackend.moderation.models.ModerationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModerationResultRepository extends JpaRepository<ModerationResult, Long> {

    Optional<ModerationResult> findByContentUuid(String contentUuid);

    List<ModerationResult> findAllByContentType(ContentType contentType);

    List<ModerationResult> findAllByDecision(ModerationDecision decision);

    List<ModerationResult> findAllByContentTypeAndDecision(ContentType contentType,
                                                            ModerationDecision decision);
}

package com.infsis.socialpagebackend.sections.repositories;

import com.infsis.socialpagebackend.sections.models.Link;
import com.infsis.socialpagebackend.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {
    List<Link> findAllByOwnerTypeAndOwnerUuid(OwnerType ownerType, String ownerUuid);
    List<Link> findAllByOwnerTypeAndOwnerUuidAndDeletedFalse(OwnerType ownerType, String ownerUuid);

    @Modifying
    @Transactional
    @Query("DELETE FROM Link l WHERE l.ownerType = :ownerType AND l.ownerUuid IN :ownerUuids")
    void hardDeleteByOwnerTypeAndOwnerUuids(@Param("ownerType") OwnerType ownerType, @Param("ownerUuids") List<String> ownerUuids);
}

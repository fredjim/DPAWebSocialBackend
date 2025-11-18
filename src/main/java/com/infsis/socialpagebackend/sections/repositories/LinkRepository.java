package com.infsis.socialpagebackend.sections.repositories;

import com.infsis.socialpagebackend.sections.models.Link;
import com.infsis.socialpagebackend.enums.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {
    List<Link> findAllByOwnerTypeAndOwnerUuid(OwnerType ownerType, String ownerUuid);
    List<Link> findAllByOwnerTypeAndOwnerUuidAndDeletedFalse(OwnerType ownerType, String ownerUuid);
}

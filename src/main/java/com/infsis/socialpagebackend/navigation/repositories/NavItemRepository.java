package com.infsis.socialpagebackend.navigation.repositories;

import com.infsis.socialpagebackend.navigation.models.NavItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NavItemRepository extends JpaRepository<NavItem, Integer> {

    @Query("SELECT n FROM NavItem n WHERE n.uuid = ?1")
    NavItem findOneByUuid(String uuid);

    @Query("SELECT n FROM NavItem n WHERE n.institution.uuid = ?1")
    List<NavItem> findByInstitutionUuid(String institutionUuid);

    @Query("SELECT n FROM NavItem n WHERE n.institution.uuid = ?1 ORDER BY n.orderIndex ASC")
    List<NavItem> findByInstitutionUuidOrderByOrderIndexAsc(String institutionUuid);
}


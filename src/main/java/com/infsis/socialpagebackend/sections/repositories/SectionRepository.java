package com.infsis.socialpagebackend.sections.repositories;

import com.infsis.socialpagebackend.sections.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Query("SELECT s FROM Section s WHERE s.uuid = ?1")
    Section findOneByUuid(String uuid);

    @Query("SELECT s FROM Section s WHERE s.navItem.uuid = ?1 AND s.deleted = false ORDER BY s.createdDate ASC")
    List<Section> findByNavItemUuid(String navItemUuid);

    @Query("SELECT s FROM Section s WHERE s.institution.uuid = ?1 AND s.deleted = false ORDER BY s.createdDate ASC")
    List<Section> findByInstitutionUuid(String institutionUuid);

    @Query("SELECT s FROM Section s WHERE s.institution.uuid = ?1 AND s.path = ?2 AND s.deleted = false")
    Optional<Section> findByInstitutionUuidAndPath(String institutionUuid, String path);

    @Query("SELECT COUNT(s) > 0 FROM Section s WHERE s.institution.uuid = ?1 AND s.path = ?2 AND s.deleted = false")
    boolean existsByInstitutionUuidAndPath(String institutionUuid, String path);

    @Query("SELECT COUNT(s) > 0 FROM Section s WHERE s.institution.uuid = ?1 AND s.path = ?2 AND s.uuid <> ?3 AND s.deleted = false")
    boolean existsByInstitutionUuidAndPathAndUuidNot(String institutionUuid, String path, String uuid);
}

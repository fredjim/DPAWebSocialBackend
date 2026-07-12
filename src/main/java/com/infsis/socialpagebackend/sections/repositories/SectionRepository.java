package com.infsis.socialpagebackend.sections.repositories;

import com.infsis.socialpagebackend.sections.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Query("SELECT s FROM Section s WHERE s.uuid = ?1")
    Section findOneByUuid(String uuid);

    @Query("SELECT s FROM Section s WHERE s.navItem.uuid = ?1 AND s.deleted = false ORDER BY s.orderIndex ASC")
    List<Section> findByNavItemUuid(String navItemUuid);

    @Query("SELECT s FROM Section s WHERE s.institution.uuid = ?1 AND s.deleted = false ORDER BY s.orderIndex ASC")
    List<Section> findByInstitutionUuid(String institutionUuid);

    @Query("SELECT COALESCE(MAX(s.orderIndex), 0) FROM Section s WHERE s.navItem.uuid = ?1 AND s.deleted = false")
    Integer findMaxOrderIndexByNavItemUuid(String navItemUuid);

    @Query("SELECT COALESCE(MAX(s.orderIndex), 0) FROM Section s WHERE s.navItem IS NULL AND s.institution.uuid = ?1 AND s.deleted = false")
    Integer findMaxOrderIndexByInstitutionUuidAndNoNavItem(String institutionUuid);

    // Hace espacio para insertar/mover una section a `fromIndex`: desplaza +1 todo lo que esté en o después de esa posición.
    // excludeUuid permite ignorar la propia section cuando se está moviendo dentro del mismo grupo (pasar null en creación).
    @Modifying
    @Transactional
    @Query("UPDATE Section s SET s.orderIndex = s.orderIndex + 1 " +
            "WHERE s.navItem.uuid = ?1 AND s.orderIndex >= ?2 AND s.deleted = false AND (?3 IS NULL OR s.uuid <> ?3)")
    void incrementOrderIndexFrom(String navItemUuid, int fromIndex, String excludeUuid);

    @Modifying
    @Transactional
    @Query("UPDATE Section s SET s.orderIndex = s.orderIndex + 1 " +
            "WHERE s.navItem IS NULL AND s.institution.uuid = ?1 AND s.orderIndex >= ?2 AND s.deleted = false AND (?3 IS NULL OR s.uuid <> ?3)")
    void incrementOrderIndexFromNoNavItem(String institutionUuid, int fromIndex, String excludeUuid);

    // Cierra el hueco dejado al remover/mover una section de `afterIndex`: desplaza -1 todo lo que esté después de esa posición.
    @Modifying
    @Transactional
    @Query("UPDATE Section s SET s.orderIndex = s.orderIndex - 1 " +
            "WHERE s.navItem.uuid = ?1 AND s.orderIndex > ?2 AND s.deleted = false AND (?3 IS NULL OR s.uuid <> ?3)")
    void decrementOrderIndexAfter(String navItemUuid, int afterIndex, String excludeUuid);

    @Modifying
    @Transactional
    @Query("UPDATE Section s SET s.orderIndex = s.orderIndex - 1 " +
            "WHERE s.navItem IS NULL AND s.institution.uuid = ?1 AND s.orderIndex > ?2 AND s.deleted = false AND (?3 IS NULL OR s.uuid <> ?3)")
    void decrementOrderIndexAfterNoNavItem(String institutionUuid, int afterIndex, String excludeUuid);

    @Query("SELECT s FROM Section s WHERE s.institution.uuid = ?1 AND s.path = ?2 AND s.deleted = false")
    Optional<Section> findByInstitutionUuidAndPath(String institutionUuid, String path);

    @Query("SELECT COUNT(s) > 0 FROM Section s WHERE s.institution.uuid = ?1 AND s.path = ?2 AND s.deleted = false")
    boolean existsByInstitutionUuidAndPath(String institutionUuid, String path);

    @Query("SELECT COUNT(s) > 0 FROM Section s WHERE s.institution.uuid = ?1 AND s.path = ?2 AND s.uuid <> ?3 AND s.deleted = false")
    boolean existsByInstitutionUuidAndPathAndUuidNot(String institutionUuid, String path, String uuid);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM section WHERE uuid = ?1")
    void hardDeleteByUuid(String uuid);
}

package com.infsis.socialpagebackend.authentication.repositories;

import com.infsis.socialpagebackend.authentication.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    @Query("SELECT u FROM Users u WHERE u.uuid = ?1")
    Users findOneByUuid(String Uuid);

    @Query("SELECT u FROM Users u WHERE u.id = ?1")
    Users findOneById(Integer id);

    Optional<Users> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<Users> findByUuid(String uuid);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE r.name = ?1")
    List<Users> findAllByRoleName(String roleName);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE r.name = ?1 AND u.institutionId = ?2")
    List<Users> findAllByRoleNameAndInstitutionId(String roleName, String institutionId);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE u.uuid = ?1 AND r.name = ?2")
    Optional<Users> findByUuidAndRoleName(String uuid, String roleName);

    // ── Gestión genérica de usuarios (paginada con filtros) ──────────────────
    // JPQL permite que Spring Data traduzca correctamente los campos de ordenamiento
    // (ej: lastName → last_name) sin necesidad de SQL nativo ni traducción manual.

    @Query("""
            SELECT u FROM Users u
            WHERE u.isRoot = false
              AND (:institutionId IS NULL OR u.institutionId = :institutionId)
              AND (:roleName IS NULL OR EXISTS (
                      SELECT r FROM u.roles r WHERE r.name = :roleName))
              AND (:enabled IS NULL OR u.enabled = :enabled)
              AND (:search IS NULL OR (
                      LOWER(u.name)     LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR
                      LOWER(u.lastName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR
                      LOWER(u.email)    LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))))
            """)
    Page<Users> findAllWithFilters(
            @Param("institutionId") String institutionId,
            @Param("roleName")      String roleName,
            @Param("enabled")       Boolean enabled,
            @Param("search")        String search,
            Pageable pageable);
}


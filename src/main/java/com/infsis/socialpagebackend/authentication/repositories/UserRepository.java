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

    @Query(value = """
            SELECT u.* FROM users u
            WHERE u.is_root = false
              AND (CAST(:institutionId AS text) IS NULL OR u.institution_id = CAST(:institutionId AS text))
              AND (CAST(:roleName AS text) IS NULL OR EXISTS (
                      SELECT 1 FROM user_roles ur
                      JOIN role r ON r.id_role = ur.role_id
                      WHERE r.name = CAST(:roleName AS text)
                        AND ur.user_id = u.id_user))
              AND (CAST(:enabled AS text) IS NULL OR u.enabled = CAST(:enabled AS boolean))
              AND (CAST(:search AS text) IS NULL OR (
                      lower(u.name)      LIKE lower(CONCAT('%', CAST(:search AS text), '%')) OR
                      lower(u.last_name) LIKE lower(CONCAT('%', CAST(:search AS text), '%')) OR
                      lower(u.email)     LIKE lower(CONCAT('%', CAST(:search AS text), '%'))))
            """,
            countQuery = """
            SELECT COUNT(u.id_user) FROM users u
            WHERE u.is_root = false
              AND (CAST(:institutionId AS text) IS NULL OR u.institution_id = CAST(:institutionId AS text))
              AND (CAST(:roleName AS text) IS NULL OR EXISTS (
                      SELECT 1 FROM user_roles ur
                      JOIN role r ON r.id_role = ur.role_id
                      WHERE r.name = CAST(:roleName AS text)
                        AND ur.user_id = u.id_user))
              AND (CAST(:enabled AS text) IS NULL OR u.enabled = CAST(:enabled AS boolean))
              AND (CAST(:search AS text) IS NULL OR (
                      lower(u.name)      LIKE lower(CONCAT('%', CAST(:search AS text), '%')) OR
                      lower(u.last_name) LIKE lower(CONCAT('%', CAST(:search AS text), '%')) OR
                      lower(u.email)     LIKE lower(CONCAT('%', CAST(:search AS text), '%'))))
            """,
            nativeQuery = true)
    Page<Users> findAllWithFilters(
            @Param("institutionId") String institutionId,
            @Param("roleName")      String roleName,
            @Param("enabled")       Boolean enabled,
            @Param("search")        String search,
            Pageable pageable);
}


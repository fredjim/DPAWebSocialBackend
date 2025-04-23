package com.infsis.socialpagebackend.authentication.repositories;

import com.infsis.socialpagebackend.authentication.models.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, Long> {
    Optional<Permissions> findByNamePermission(String namePermission);
}
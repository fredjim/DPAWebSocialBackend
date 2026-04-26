package com.infsis.socialpagebackend.social_networks.repositories;

import com.infsis.socialpagebackend.social_networks.models.InstitutionFacebookConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstitutionFacebookConfigRepository
        extends JpaRepository<InstitutionFacebookConfig, Integer> {

    Optional<InstitutionFacebookConfig> findByInstitutionUuid(String institutionUuid);

    boolean existsByInstitutionUuid(String institutionUuid);
}

package com.incident.service.repository;

import com.incident.service.entity.GestionnaireSLA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GestionnaireSLARepository extends JpaRepository<GestionnaireSLA, Long> {

    Optional<GestionnaireSLA> findBySeverite(String severite);
}


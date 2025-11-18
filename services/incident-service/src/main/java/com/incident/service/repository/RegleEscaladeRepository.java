package com.incident.service.repository;

import com.incident.service.entity.RegleEscalade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegleEscaladeRepository extends JpaRepository<RegleEscalade, Long> {

    List<RegleEscalade> findBySeveriteAndEstActiveOrderByNiveauEscalade(String severite, Boolean estActive);

    List<RegleEscalade> findByIncidentId(Long incidentId);

    List<RegleEscalade> findByRoleCible(String roleCible);
}


package com.incident.service.repository;

import com.incident.service.entity.EtapeFluxTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtapeFluxTravailRepository extends JpaRepository<EtapeFluxTravail, Long> {

    List<EtapeFluxTravail> findByIncidentIdOrderByOrdreEtape(Long incidentId);

    List<EtapeFluxTravail> findByStatut(String statut);

    List<EtapeFluxTravail> findByExecutePar(String executePar);
}


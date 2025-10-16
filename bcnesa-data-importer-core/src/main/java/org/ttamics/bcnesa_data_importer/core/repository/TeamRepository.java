package org.ttamics.bcnesa_data_importer.core.repository;

import org.ttamics.bcnesa_data_importer.core.model.Team;

import java.util.Optional;

public interface TeamRepository {
    Team findById(String id);
    Optional<Team> findByName(String name);
    boolean existsById(String id);
    boolean existsByName(String name);
    void save(Team team);
}

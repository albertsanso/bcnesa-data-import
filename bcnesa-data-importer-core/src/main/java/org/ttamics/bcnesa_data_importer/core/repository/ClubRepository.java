package org.ttamics.bcnesa_data_importer.core.repository;

import org.ttamics.bcnesa_data_importer.core.model.Club;

import java.util.List;
import java.util.Optional;

public interface ClubRepository {
    Optional<Club> findById(String id);
    Optional<Club> findByName(String name);
    List<Club> findAll();
    boolean existsById(String id);
    boolean existsByName(String name);
    void save(Club club);
}

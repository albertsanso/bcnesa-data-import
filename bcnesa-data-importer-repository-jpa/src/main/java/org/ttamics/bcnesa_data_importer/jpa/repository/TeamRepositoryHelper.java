package org.ttamics.bcnesa_data_importer.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ttamics.bcnesa_data_importer.jpa.model.TeamJPA;

import java.util.Optional;

public interface TeamRepositoryHelper extends JpaRepository<TeamJPA, String> {
    Optional<TeamJPA> findByName(String name);
    boolean existsByName(String name);
}

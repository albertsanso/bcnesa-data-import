package org.ttamics.bcnesa_data_importer.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.ttamics.bcnesa_data_importer.core.model.Club;
import org.ttamics.bcnesa_data_importer.jpa.model.ClubJPA;

import java.util.Optional;

@Repository
public interface ClubRepositoryHelper extends CrudRepository<ClubJPA, String> {
    Optional<ClubJPA> findByName(String name);
    boolean existsByName(String name);
}

package org.ttamics.bcnesa_data_importer.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.ttamics.bcnesa_data_importer.jpa.model.PracticionerJPA;

import java.util.Optional;

public interface PracticionerRepositoryHelper extends CrudRepository<PracticionerJPA, String> {
    Optional<PracticionerJPA> findByFullName(String fullName);
}

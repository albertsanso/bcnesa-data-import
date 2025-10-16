package org.ttamics.bcnesa_data_importer.core.repository;

import org.ttamics.bcnesa_data_importer.core.model.Practicioner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PracticionerRepository {

    Optional<Practicioner> findById(UUID id);
    Optional<Practicioner> findByFullName(String fullName);
    List<Practicioner> findAll();
    void save(Practicioner practicioner);
}

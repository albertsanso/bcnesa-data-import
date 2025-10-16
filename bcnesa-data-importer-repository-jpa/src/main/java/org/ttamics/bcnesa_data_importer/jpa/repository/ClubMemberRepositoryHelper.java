package org.ttamics.bcnesa_data_importer.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.ttamics.bcnesa_data_importer.jpa.model.ClubMemberJPA;

import java.util.Optional;
import java.util.UUID;

public interface ClubMemberRepositoryHelper extends CrudRepository<ClubMemberJPA, String> {
    Optional<ClubMemberJPA> findByPracticionerIdAndClubId(UUID practicionerId, UUID clubId);
}

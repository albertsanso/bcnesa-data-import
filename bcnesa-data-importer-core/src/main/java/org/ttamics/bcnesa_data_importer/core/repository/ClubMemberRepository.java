package org.ttamics.bcnesa_data_importer.core.repository;

import org.ttamics.bcnesa_data_importer.core.model.ClubMember;

import java.util.Optional;
import java.util.UUID;

public interface ClubMemberRepository {

    Optional<ClubMember> findByPracticionerIdAndClubId(UUID practicionerId, UUID clubId);
    void save(ClubMember clubMember);
}

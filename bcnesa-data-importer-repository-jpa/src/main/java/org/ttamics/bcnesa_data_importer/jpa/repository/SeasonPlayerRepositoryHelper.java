package org.ttamics.bcnesa_data_importer.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ttamics.bcnesa_data_importer.jpa.model.SeasonPlayerJPA;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeasonPlayerRepositoryHelper extends JpaRepository<SeasonPlayerJPA, String> {
    Optional<SeasonPlayerJPA> findByClubMember_Practicioner_IdAndClubMember_Club_IdAndYearRange(
            UUID practicionerId,
            UUID clubId,
            String yearRange
    );

    Optional<SeasonPlayerJPA> findByClubMember_Practicioner_FullNameAndClubMember_Club_NameAndYearRange(
            String practicionerName,
            String clubName,
            String yearRange
    );
}

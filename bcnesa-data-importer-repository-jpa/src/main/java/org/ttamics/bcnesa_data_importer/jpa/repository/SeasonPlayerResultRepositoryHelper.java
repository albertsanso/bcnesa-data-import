package org.ttamics.bcnesa_data_importer.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ttamics.bcnesa_data_importer.core.model.Competition;
import org.ttamics.bcnesa_data_importer.core.model.CompetitionType;
import org.ttamics.bcnesa_data_importer.core.model.LicenseType;
import org.ttamics.bcnesa_data_importer.jpa.model.SeasonPlayerResultJPA;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeasonPlayerResultRepositoryHelper extends JpaRepository<SeasonPlayerResultJPA, UUID>
{
    Optional<SeasonPlayerResultJPA> findBySeasonAndCompetitionTypeAndCompetitionAndJornadaAndGroupAndSeasonPlayer_ClubMember_Club_NameAndSeasonPlayer_LicenseTypeAndSeasonPlayer_LicenseRefAndMatchLinkageId(
            String season,
            CompetitionType competitionType,
            Competition competition,
            String jornada,
            String group,
            String teamName,
            LicenseType licenseType,
            String licenseId,
            String gameLetters);
}

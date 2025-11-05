package org.ttamics.bcnesa_data_importer.core.repository;

import org.ttamics.bcnesa_data_importer.core.model.Competition;
import org.ttamics.bcnesa_data_importer.core.model.CompetitionType;
import org.ttamics.bcnesa_data_importer.core.model.LicenseType;
import org.ttamics.bcnesa_data_importer.core.model.SeasonPlayerResult;

import java.util.Optional;
import java.util.UUID;

public interface SeasonPlayerResultRepository {
    Optional<SeasonPlayerResult> findById(UUID id);
    Optional<SeasonPlayerResult> findFor(
            String season,
            CompetitionType competitionType,
            Competition competition,
            String jornada,
            String group,
            String clubName,
            LicenseType licenseType,
            String licenseId,
            String gameLetters
    );
    void save(SeasonPlayerResult seasonPlayerResult);
}

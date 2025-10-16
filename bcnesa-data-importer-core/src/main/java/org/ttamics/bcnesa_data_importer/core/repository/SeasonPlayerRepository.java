package org.ttamics.bcnesa_data_importer.core.repository;

import org.ttamics.bcnesa_data_importer.core.model.SeasonPlayer;

import java.util.Optional;
import java.util.UUID;

public interface SeasonPlayerRepository {
    Optional<SeasonPlayer> findById(String id);
    Optional<SeasonPlayer> findByPracticionerIdClubIdSeason(UUID practicionerId, UUID clubId, String season);
    Optional<SeasonPlayer> findByPracticionerNameAndClubNameAndSeason(String practicionerName, String clubName, String season);
    void save(SeasonPlayer seasonPlayer);
}

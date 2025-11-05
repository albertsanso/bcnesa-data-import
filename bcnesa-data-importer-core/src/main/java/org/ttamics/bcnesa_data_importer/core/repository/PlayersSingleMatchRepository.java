package org.ttamics.bcnesa_data_importer.core.repository;

import org.ttamics.bcnesa_data_importer.core.model.PlayersSingleMatch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayersSingleMatchRepository {
    Optional<PlayersSingleMatch> findById(UUID id);
    Optional<PlayersSingleMatch> findBySeasonPlayerResultAbcIdAndSeasonPlayerResultXyzIdAndUniqueId(UUID seasonPlayerResultAbcId, UUID seasonPlayerResultXyzId, String uniqueId);
    List<PlayersSingleMatch> findAll();
    void save(PlayersSingleMatch playerSingleMatch);
}

package org.ttamics.bcnesa_data_importer.jpa.repository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.PlayersSingleMatch;
import org.ttamics.bcnesa_data_importer.core.repository.PlayersSingleMatchRepository;
import org.ttamics.bcnesa_data_importer.jpa.mapper.PlayersSingleMatchJPAToPlayersSingleMatchMapper;
import org.ttamics.bcnesa_data_importer.jpa.mapper.PlayersSingleMatchToPlayersSingleMatchJPAMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class PlayersSingleMatchRepositoryImpl implements PlayersSingleMatchRepository {

    private final PlayersSingleMatchToPlayersSingleMatchJPAMapper toJPAMapper;

    private final PlayersSingleMatchJPAToPlayersSingleMatchMapper fromJPAMapper;

    private final PlayersSingleMatchRepositoryHelper helper;

    @Autowired
    public PlayersSingleMatchRepositoryImpl(PlayersSingleMatchToPlayersSingleMatchJPAMapper toJPAMapper, PlayersSingleMatchJPAToPlayersSingleMatchMapper fromJPAMapper, PlayersSingleMatchRepositoryHelper helper) {
        this.toJPAMapper = toJPAMapper;
        this.fromJPAMapper = fromJPAMapper;
        this.helper = helper;
    }

    @Override
    public Optional<PlayersSingleMatch> findById(UUID id) {
        return helper.findById(id)
                .map(fromJPAMapper);
    }

    @Override
    public Optional<PlayersSingleMatch> findBySeasonPlayerResultAbcIdAndSeasonPlayerResultXyzIdAndUniqueId(UUID seasonPlayerResultAbcId, UUID seasonPlayerResultXyzId, String uniqueId) {
        return helper.findBySeasonPlayerResultAbc_IdAndSeasonPlayerResultXyz_IdAndUniqueRowMatchId(seasonPlayerResultAbcId, seasonPlayerResultXyzId, uniqueId)
                .map(fromJPAMapper);
    }

    @Override
    public List<PlayersSingleMatch> findAll() {
        return helper.findAll().stream().map(fromJPAMapper).toList();
    }

    @Override
    public void save(PlayersSingleMatch playerSingleMatch) {
        helper.save(toJPAMapper.apply(playerSingleMatch));
    }
}

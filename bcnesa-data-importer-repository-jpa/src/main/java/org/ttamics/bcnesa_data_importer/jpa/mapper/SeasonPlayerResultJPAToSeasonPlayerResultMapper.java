package org.ttamics.bcnesa_data_importer.jpa.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.SeasonPlayerResult;
import org.ttamics.bcnesa_data_importer.jpa.model.SeasonPlayerResultJPA;

import java.util.function.Function;

@Component
public class SeasonPlayerResultJPAToSeasonPlayerResultMapper implements Function<SeasonPlayerResultJPA, SeasonPlayerResult> {

    private final SeasonPlayerJPAToSeasonPlayerMapper seasonPlayerJPAToSeasonPlayerMapper;

    @Autowired
    public SeasonPlayerResultJPAToSeasonPlayerResultMapper(SeasonPlayerJPAToSeasonPlayerMapper seasonPlayerJPAToSeasonPlayerMapper) {
        this.seasonPlayerJPAToSeasonPlayerMapper = seasonPlayerJPAToSeasonPlayerMapper;
    }

    @Override
    public SeasonPlayerResult apply(SeasonPlayerResultJPA seasonPlayerResultJPA) {
        return SeasonPlayerResult.createExisting(
                seasonPlayerResultJPA.getId(),
                seasonPlayerResultJPA.getSeason(),
                seasonPlayerResultJPA.getCompetitionType(),
                seasonPlayerResultJPA.getCompetition(),
                seasonPlayerResultJPA.getJornada(),
                seasonPlayerResultJPA.getGroup(),
                seasonPlayerJPAToSeasonPlayerMapper.apply(seasonPlayerResultJPA.getSeasonPlayer()),
                seasonPlayerResultJPA.getPlayerLetter(),
                seasonPlayerResultJPA.getGamePoints().stream().mapToInt(Integer::parseInt).toArray(),
                seasonPlayerResultJPA.getGamesWon(),
                seasonPlayerResultJPA.getMatchLinkageId()
        );
    }
}

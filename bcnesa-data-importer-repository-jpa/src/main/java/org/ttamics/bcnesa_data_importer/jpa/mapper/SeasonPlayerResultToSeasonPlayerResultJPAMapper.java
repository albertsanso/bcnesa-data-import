package org.ttamics.bcnesa_data_importer.jpa.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.SeasonPlayerResult;
import org.ttamics.bcnesa_data_importer.jpa.model.SeasonPlayerResultJPA;

import java.util.Arrays;
import java.util.function.Function;

@Component
public class SeasonPlayerResultToSeasonPlayerResultJPAMapper implements Function<SeasonPlayerResult, SeasonPlayerResultJPA> {
    private final SeasonPlayerToSeasonPlayerJPAMapper seasonPlayerToSeasonPlayerJPAMapper;

    @Autowired
    public SeasonPlayerResultToSeasonPlayerResultJPAMapper(SeasonPlayerToSeasonPlayerJPAMapper seasonPlayerToSeasonPlayerJPAMapper) {
        this.seasonPlayerToSeasonPlayerJPAMapper = seasonPlayerToSeasonPlayerJPAMapper;
    }

    @Override
    public SeasonPlayerResultJPA apply(SeasonPlayerResult seasonPlayerResult) {
        SeasonPlayerResultJPA resultJpa = new SeasonPlayerResultJPA();
        resultJpa.setId(seasonPlayerResult.getId());
        resultJpa.setSeason(seasonPlayerResult.getSeason());
        resultJpa.setCompetitionType(seasonPlayerResult.getCompetitionType());
        resultJpa.setCompetition(seasonPlayerResult.getCompetition());
        resultJpa.setJornada(seasonPlayerResult.getJornada());
        resultJpa.setGroup(seasonPlayerResult.getGroup());
        resultJpa.setPlayerLetter(seasonPlayerResult.getPlayerLetter());
        resultJpa.setSeasonPlayer(seasonPlayerToSeasonPlayerJPAMapper.apply(seasonPlayerResult.getSeasonPlayer()));
        resultJpa.setGamePoints(Arrays.stream(seasonPlayerResult.getGamePoints())
                .mapToObj(String::valueOf).toList());
        resultJpa.setGamesWon(seasonPlayerResult.getGamesWon());
        resultJpa.setMatchLinkageId(seasonPlayerResult.getMatchLinkageId());
        return resultJpa;
    }
}

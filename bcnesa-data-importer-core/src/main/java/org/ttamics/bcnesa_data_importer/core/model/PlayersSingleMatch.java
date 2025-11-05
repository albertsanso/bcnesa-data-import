package org.ttamics.bcnesa_data_importer.core.model;

import java.util.UUID;

public class PlayersSingleMatch {

    private final UUID id;

    private final SeasonPlayerResult seasonPlayerResultAbc;

    private final SeasonPlayerResult seasonPlayerResultXyz;

    private final MatchContext matchContext;

    private final String uniqueRowMatchId;

    private PlayersSingleMatch(UUID id, SeasonPlayerResult seasonPlayerResultAbc, SeasonPlayerResult seasonPlayerResultXyz, MatchContext matchContext, String uniqueRowMatchId) {
        this.id = id;
        this.seasonPlayerResultAbc = seasonPlayerResultAbc;
        this.seasonPlayerResultXyz = seasonPlayerResultXyz;
        this.matchContext = matchContext;
        this.uniqueRowMatchId = uniqueRowMatchId;
    }


    public static PlayersSingleMatch createNew(SeasonPlayerResult seasonPlayerResultAbc, SeasonPlayerResult seasonPlayerResultXyz, MatchContext matchContext, String uniqueRowMatchId) {
        return new PlayersSingleMatch(UUID.randomUUID(), seasonPlayerResultAbc, seasonPlayerResultXyz, matchContext, uniqueRowMatchId);
    }

    public static PlayersSingleMatch createExisting(UUID id, SeasonPlayerResult seasonPlayerResultAbc, SeasonPlayerResult seasonPlayerResultXyz, MatchContext matchContext, String uniqueRowMatchId) {
        return new PlayersSingleMatch(id, seasonPlayerResultAbc, seasonPlayerResultXyz, matchContext, uniqueRowMatchId);
    }

    public UUID getId() {
        return id;
    }

    public SeasonPlayerResult getSeasonPlayerResultAbc() {
        return seasonPlayerResultAbc;
    }

    public SeasonPlayerResult getSeasonPlayerResultXyz() {
        return seasonPlayerResultXyz;
    }

    public MatchContext getMatchContext() {
        return matchContext;
    }

    public String getUniqueRowMatchId() {
        return uniqueRowMatchId;
    }
}

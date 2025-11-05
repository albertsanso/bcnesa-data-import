package org.ttamics.bcnesa_data_importer.core.model;

public class MatchContext {
    private final String season;

    private final CompetitionType competitionType;

    private final Competition competition;

    private final String jornada;

    private final String group;

    public MatchContext(String season, CompetitionType competitionType, Competition competition, String jornada, String group) {
        this.season = season;
        this.competitionType = competitionType;
        this.competition = competition;
        this.jornada = jornada;
        this.group = group;
    }

    public String getSeason() {
        return season;
    }

    public CompetitionType getCompetitionType() {
        return competitionType;
    }

    public Competition getCompetition() {
        return competition;
    }

    public String getJornada() {
        return jornada;
    }

    public String getGroup() {
        return group;
    }
}

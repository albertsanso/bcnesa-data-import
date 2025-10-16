package org.ttamics.bcnesa_data_importer.core.model;

public class ClubPlayer {
    private final SeasonPlayer seasonPlayer;
    private final Club club;

    private ClubPlayer(SeasonPlayer seasonPlayer, Club club) {
        this.seasonPlayer = seasonPlayer;
        this.club = club;
    }

    public SeasonPlayer getPlayer() {
        return seasonPlayer;
    }

    public Club getClub() {
        return club;
    }
}

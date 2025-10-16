package org.ttamics.bcnesa_data_importer.core.model;

public class LicensedPlayer {
    private final String licenseId;
    private final ClubMember clubMember;
    private final String season;
    private final Competition competition;

    private LicensedPlayer(String licenseId, ClubMember clubMember, String season, Competition competition) {
        this.licenseId = licenseId;
        this.clubMember = clubMember;
        this.season = season;
        this.competition = competition;
    }

    public static LicensedPlayer createNew(String licenseId, ClubMember clubMember, String season, Competition competition) {
        return new LicensedPlayer(licenseId, clubMember, season, competition);
    }

    public String getLicenseId() {
        return licenseId;
    }

    public String getSeason() {
        return season;
    }

    public Competition getCompetition() {
        return competition;
    }

    public ClubMember getClubPlayer() {
        return clubMember;
    }
}

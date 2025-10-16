package org.ttamics.bcnesa_data_importer.core.model;

public enum Competition {
    BCN_SENIOR_PROVINCIAL_4A(CompetitionType.SENIOR, "4"),
    BNC_SENIOR_PROVINCIAL_3A_B(CompetitionType.SENIOR, "3,B"),
    BNC_SENIOR_PROVINCIAL_3A_A(CompetitionType.SENIOR, "3,A"),
    BNC_SENIOR_PROVINCIAL_2A_B(CompetitionType.SENIOR, "2,B"),
    BNC_SENIOR_PROVINCIAL_2A_A(CompetitionType.SENIOR, "2,A"),
    BNC_SENIOR_PROVINCIAL_1A(CompetitionType.SENIOR, "1,A"),
    BCN_SENIOR_PREFERENT(CompetitionType.PREFERENT, "1,A"),
    BCN_VETERANS_4A_B(CompetitionType.VETERANS, "4,B"),
    BCN_VETERANS_4A_A(CompetitionType.VETERANS, "4,A"),
    BCN_VETERANS_3A_B(CompetitionType.VETERANS, "3,B"),
    BCN_VETERANS_3A_A(CompetitionType.VETERANS, "3,A"),
    BCN_VETERANS_2A_B(CompetitionType.VETERANS, "2,B"),
    BCN_VETERANS_2A_A(CompetitionType.VETERANS, "2,A"),
    BCN_VETERANS_1A(CompetitionType.VETERANS, "1");

    private final CompetitionType competitionType;

    private final String competitionLevel;

    Competition(CompetitionType competitionType, String competitionLevel) {
        this.competitionType = competitionType;
        this.competitionLevel = competitionLevel;
    }
    public CompetitionType getCompetitionType() {
        return competitionType;
    }

    public String getCompetitionLevel() {
        return competitionLevel;
    }
}

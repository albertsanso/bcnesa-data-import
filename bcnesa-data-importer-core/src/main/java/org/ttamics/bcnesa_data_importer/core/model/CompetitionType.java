package org.ttamics.bcnesa_data_importer.core.model;

import java.util.Optional;

public enum CompetitionType {
    PREFERENT("preferent"),
    SENIOR("senior"),
    VETERANS("veterans");

    private final String value;

    CompetitionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static Optional<CompetitionType> findByValue(String value) {
        for (CompetitionType competitionType : CompetitionType.values()) {
            if (competitionType.value.equals(value)) {
                return Optional.of(competitionType);
            }
        }
        return Optional.empty();
    }

    public static CompetitionType fromValue(String value) {
        return findByValue(value).orElseThrow(() -> new IllegalArgumentException("Unknown CompetitionType: " + value));

    }

    public static boolean existsByValue(String value) {
        return findByValue(value).isPresent();
    }
}

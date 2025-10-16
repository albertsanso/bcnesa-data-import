package org.ttamics.bcnesa_data_importer.core.model;

public enum Season {
    SEASON_2024_2025("2024-2025"),
    SEASON_2023_2024("2023-2024"),
    SEASON_2022_2023("2022-2023"),
    SEASON_2021_2022("2021-2022"),
    SEASON_2020_2021("2020-2021"),
    SEASON_2019_2020("2019-2020"),
    SEASON_2018_2019("2018-2019");

    private final String value;

    Season(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Season fromValue(String value) {
        for (Season season : Season.values()) {
            if (season.value.equalsIgnoreCase(value)) {
                return season;
            }
        }
        throw new IllegalArgumentException("Unknown Season: " + value);
    }
}

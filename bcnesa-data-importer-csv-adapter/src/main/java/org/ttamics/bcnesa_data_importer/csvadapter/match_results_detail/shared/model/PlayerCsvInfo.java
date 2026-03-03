package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model;

public record PlayerCsvInfo(
        String teamName,
        String playerLetter,
        String playerLicense,
        String playerName,
        int playerScore) {
}

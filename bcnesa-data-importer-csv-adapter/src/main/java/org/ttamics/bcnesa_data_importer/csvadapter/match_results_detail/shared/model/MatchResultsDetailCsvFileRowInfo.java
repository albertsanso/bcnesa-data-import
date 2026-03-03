package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model;

import java.util.UUID;

public record MatchResultsDetailCsvFileRowInfo(
        MatchResultsDetailCsvFileInfo fileInfo,
        String[] rowInfo,
        UUID uniqueRowId
) {
}

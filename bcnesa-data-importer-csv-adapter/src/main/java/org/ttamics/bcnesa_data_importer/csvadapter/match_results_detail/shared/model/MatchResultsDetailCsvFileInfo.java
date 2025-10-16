package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model;

import org.ttamics.bcnesa_data_importer.core.model.Competition;
import org.ttamics.bcnesa_data_importer.core.model.CompetitionType;

import java.nio.file.Path;

public record MatchResultsDetailCsvFileInfo(Path csvFilepath, String season, CompetitionType competitionType, Competition competition, String jornada, String group) {
}

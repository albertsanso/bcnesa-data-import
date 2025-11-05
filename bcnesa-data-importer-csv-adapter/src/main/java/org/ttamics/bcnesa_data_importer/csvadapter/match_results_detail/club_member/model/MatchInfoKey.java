package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club_member.model;

import org.ttamics.bcnesa_data_importer.core.model.Competition;
import org.ttamics.bcnesa_data_importer.core.model.CompetitionType;

public record MatchInfoKey (String season,
                            CompetitionType competitionType,
                            Competition competition,
                            String jornada,
                            String group,
                            String teamNameAbc,
                            String teamNameXyz) {
}

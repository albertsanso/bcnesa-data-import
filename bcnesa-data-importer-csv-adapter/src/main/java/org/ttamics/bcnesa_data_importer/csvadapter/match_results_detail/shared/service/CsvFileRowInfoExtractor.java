package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service;

import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.PlayerCsvInfo;

@Component
public class CsvFileRowInfoExtractor {

    public String extractTeamNameFromRowInfo(MatchResultsDetailCsvFileRowInfo rowInfo) {
        return rowInfo.rowInfo()[1];
    }

    public MatchResultsDetailRowInfo extractMatchDetailsRowInfo(MatchResultsDetailCsvFileRowInfo rowInfo) {
        PlayerCsvInfo abcPlayer = parsePlayerABC(rowInfo);
        PlayerCsvInfo xyzPlayer = parsePlayerXYZ(rowInfo);
        return new MatchResultsDetailRowInfo(abcPlayer, xyzPlayer);
    }

    private PlayerCsvInfo parsePlayerABC(MatchResultsDetailCsvFileRowInfo rowInfo) {
        return new PlayerCsvInfo(
                rowInfo.rowInfo()[0],
                rowInfo.rowInfo()[2],
                rowInfo.rowInfo()[3],
                rowInfo.rowInfo()[4],
                Integer.parseInt(rowInfo.rowInfo()[5])
        );
    }

    private PlayerCsvInfo parsePlayerXYZ(MatchResultsDetailCsvFileRowInfo rowInfo) {
        return new PlayerCsvInfo(
                rowInfo.rowInfo()[1],
                rowInfo.rowInfo()[6],
                rowInfo.rowInfo()[7],
                rowInfo.rowInfo()[8],
                Integer.parseInt(rowInfo.rowInfo()[9])
        );
    }
}

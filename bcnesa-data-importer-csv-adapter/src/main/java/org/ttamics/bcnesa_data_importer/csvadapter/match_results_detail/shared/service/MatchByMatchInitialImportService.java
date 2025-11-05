package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service;

import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchByMatchInitialImportService {
    protected final MatchResultDetailsByLineIterator matchResultDetailsByLineIterator;

    public MatchByMatchInitialImportService(MatchResultDetailsByLineIterator matchResultDetailsByLineIterator) {
        this.matchResultDetailsByLineIterator = matchResultDetailsByLineIterator;
    }

    protected void resetAndLoadTextFilesForSeason(String baseSeasonsFolder, String seasonRange) throws IOException {
        matchResultDetailsByLineIterator.resetAndLoadTextFilesForSeason(new File(baseSeasonsFolder), seasonRange);
    }

    protected void resetAndLoadTextFilesForAllSeasons(String baseSeasonsFolder) throws IOException {
        matchResultDetailsByLineIterator.resetAndLoadTextFilesForAllSeasons(new File(baseSeasonsFolder));
    }

    protected List<MatchResultsDetailCsvFileRowInfo> fetchCsvRowInfos() {
        List<MatchResultsDetailCsvFileRowInfo> matchResultsDetailCsvFileRowInfoList = new ArrayList<>();
        while (matchResultDetailsByLineIterator.hasNext()) {
            MatchResultsDetailCsvFileRowInfo rowInfo = matchResultDetailsByLineIterator.next();

            MatchResultsDetailCsvFileInfo matchResultsDetailCsvFileInfo = rowInfo.fileInfo();
            matchResultsDetailCsvFileInfo.season();
            matchResultsDetailCsvFileInfo.csvFilepath();
            matchResultsDetailCsvFileInfo.competition();
            matchResultsDetailCsvFileInfo.group();
            matchResultsDetailCsvFileInfo.jornada();
            matchResultsDetailCsvFileInfo.competitionType();


            matchResultsDetailCsvFileRowInfoList.add(rowInfo);
        }
        return matchResultsDetailCsvFileRowInfoList;
    }
}

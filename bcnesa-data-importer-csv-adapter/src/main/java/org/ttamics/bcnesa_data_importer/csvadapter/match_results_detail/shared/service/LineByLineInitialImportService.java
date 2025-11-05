package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service;

import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LineByLineInitialImportService {
    protected final MatchResultDetailsByLineIterator matchResultDetailsByLineIterator;

    public LineByLineInitialImportService(MatchResultDetailsByLineIterator matchResultDetailsByLineIterator) {
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
            matchResultsDetailCsvFileRowInfoList.add(rowInfo);
        }
        return matchResultsDetailCsvFileRowInfoList;
    }
}

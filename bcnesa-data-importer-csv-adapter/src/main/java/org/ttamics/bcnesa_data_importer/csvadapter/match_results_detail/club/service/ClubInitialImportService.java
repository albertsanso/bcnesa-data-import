package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Club;
import org.ttamics.bcnesa_data_importer.core.repository.ClubRepository;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club.model.ClubNameAndYearInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.BaseInitialImportService;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.CsvFileRowInfoExtractor;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.MatchResultDetailsByLineIterator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class ClubInitialImportService extends BaseInitialImportService {

    private final ClubRepository clubRepository;

    private final CsvFileRowInfoExtractor rowInfoExtractor;

    @Autowired
    public ClubInitialImportService(MatchResultDetailsByLineIterator matchResultDetailsByLineIterator, ClubRepository clubRepository, CsvFileRowInfoExtractor rowInfoExtractor) {
        super(matchResultDetailsByLineIterator);
        this.clubRepository = clubRepository;
        this.rowInfoExtractor = rowInfoExtractor;
    }

    public void processClubNamesForSeason(String baseSeasonsFolder, String seasonRange) throws IOException {
        resetAndLoadTextFilesForSeason(baseSeasonsFolder, seasonRange);
        importClubNames();
    }

    public void processClubNamesForAllSeasons(String baseSeasonsFolder) throws IOException {
        resetAndLoadTextFilesForAllSeasons(baseSeasonsFolder);
        importClubNames();
    }

    private void importClubNames() {
        List<MatchResultsDetailCsvFileRowInfo> rowInfowsList = fetchCsvRowInfos();
        saveClubNamesInfo(rowInfowsList);
    }

    private void saveClubNamesInfo(List<MatchResultsDetailCsvFileRowInfo> matchResultsDetailCsvFileRowInfoList) {
        Map<String, List<String>> cleanClubNamesAndYears = extractClubNamesFromTeamNames(matchResultsDetailCsvFileRowInfoList);
        cleanClubNamesAndYears.keySet().forEach(cleanClubName -> {
            Club clubToCreate = Club.createNew(cleanClubName);
            cleanClubNamesAndYears.get(cleanClubName).forEach(clubToCreate::addYearRange);
            createClubIfDoesntExistYet(clubToCreate);
        });
    }

    private Map<String, List<String>> extractClubNamesFromTeamNames(List<MatchResultsDetailCsvFileRowInfo> matchResultsDetailCsvFileRowInfoList) {
        Pattern clubNameWithTeamNamePattern = Pattern.compile("(['\"]{1,2})(.)(['\"]{1,2})");

        List<ClubNameAndYearInfo> filteredTeamNames = matchResultsDetailCsvFileRowInfoList.stream()
                .filter(rowInfo -> !clubNameWithTeamNamePattern.matcher(rowInfoExtractor.extractTeamNameFromRowInfo(rowInfo)).find())
                .map(matchResultsDetailCsvFileRowInfo ->
                        new ClubNameAndYearInfo(
                                rowInfoExtractor.extractTeamNameFromRowInfo(matchResultsDetailCsvFileRowInfo),
                                matchResultsDetailCsvFileRowInfo.fileInfo().season()))
                .toList();

        return ClubNameGrouppingService.groupByCommonRoot(filteredTeamNames);
    }

    private void createClubIfDoesntExistYet(Club clubToCreate) {
        Optional<Club> existingClub = clubRepository.findByName(clubToCreate.getName());
        if (existingClub.isPresent()) {
            existingClub.get().updateAllRanges(clubToCreate.getYearRanges());
            clubRepository.save(existingClub.get());
        } else {
            clubRepository.save(clubToCreate);
        }
    }
}

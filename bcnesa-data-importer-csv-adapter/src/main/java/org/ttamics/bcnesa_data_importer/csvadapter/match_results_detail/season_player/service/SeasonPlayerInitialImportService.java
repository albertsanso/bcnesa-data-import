package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.season_player.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Club;
import org.ttamics.bcnesa_data_importer.core.model.Practicioner;
import org.ttamics.bcnesa_data_importer.core.model.SeasonPlayer;
import org.ttamics.bcnesa_data_importer.core.repository.ClubMemberRepository;
import org.ttamics.bcnesa_data_importer.core.repository.ClubRepository;
import org.ttamics.bcnesa_data_importer.core.repository.PracticionerRepository;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.PlayerCsvInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.BaseInitialImportService;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.CsvFileRowInfoExtractor;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.MatchResultDetailsByLineIterator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class SeasonPlayerInitialImportService extends BaseInitialImportService {

    private final CsvFileRowInfoExtractor rowInfoExtractor;

    private final ClubMemberRepository clubMemberRepository;

    private final PracticionerRepository practicionerRepository;

    private final ClubRepository clubRepository;

    @Autowired
    public SeasonPlayerInitialImportService(MatchResultDetailsByLineIterator matchResultDetailsByLineIterator, CsvFileRowInfoExtractor rowInfoExtractor, ClubMemberRepository clubMemberRepository, PracticionerRepository practicionerRepository, ClubRepository clubRepository) {
        super(matchResultDetailsByLineIterator);
        this.rowInfoExtractor = rowInfoExtractor;
        this.clubMemberRepository = clubMemberRepository;
        this.practicionerRepository = practicionerRepository;
        this.clubRepository = clubRepository;
    }

    public void processSeasonPlayersForSeason(String baseSeasonsFolder, String seasonRange) throws IOException {
        resetAndLoadTextFilesForSeason(baseSeasonsFolder, seasonRange);
        importSeasonPlayers();
    }

    public void processSeasonPlayersForAllSeasons(String baseSeasonsFolder) throws IOException {
        resetAndLoadTextFilesForAllSeasons(baseSeasonsFolder);
        importSeasonPlayers();
    }

    public void importSeasonPlayers() {
        List<MatchResultsDetailCsvFileRowInfo> rowInfowsList = fetchCsvRowInfos();
        saveSeasonPlayersInfo(rowInfowsList);
    }

    private void saveSeasonPlayersInfo(List<MatchResultsDetailCsvFileRowInfo> matchResultsDetailCsvFileRowInfoList) {
        List<Club> allClubsList = clubRepository.findAll();

        matchResultsDetailCsvFileRowInfoList
                .forEach(matchResultsDetailCsvFileRowInfo -> {
                    String seasonRange = matchResultsDetailCsvFileRowInfo.fileInfo().season();
                    MatchResultsDetailRowInfo rowInfo = rowInfoExtractor.extractMatchDetailsRowInfo(matchResultsDetailCsvFileRowInfo);
                    createSeasonPlayer(rowInfo.acbPlayer(), seasonRange);
                    createSeasonPlayer(rowInfo.xyzPlayer(), seasonRange);
                });
    }

    private void createSeasonPlayer(PlayerCsvInfo playerInfo, String seasonRange) {
        playerInfo.playerName();
        playerInfo.playerLetter();
        playerInfo.playerScore();
        playerInfo.playerLicense();
        playerInfo.teamName();

    }
}

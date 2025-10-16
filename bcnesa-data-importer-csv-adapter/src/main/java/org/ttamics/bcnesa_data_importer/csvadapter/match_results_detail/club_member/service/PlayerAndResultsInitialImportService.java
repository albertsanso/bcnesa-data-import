package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club_member.service;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Club;
import org.ttamics.bcnesa_data_importer.core.model.ClubMember;
import org.ttamics.bcnesa_data_importer.core.model.License;
import org.ttamics.bcnesa_data_importer.core.model.Practicioner;
import org.ttamics.bcnesa_data_importer.core.model.SeasonPlayer;
import org.ttamics.bcnesa_data_importer.core.repository.ClubMemberRepository;
import org.ttamics.bcnesa_data_importer.core.repository.ClubRepository;
import org.ttamics.bcnesa_data_importer.core.repository.PracticionerRepository;
import org.ttamics.bcnesa_data_importer.core.repository.SeasonPlayerRepository;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club.service.ClubMatchingService;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.PlayerCsvInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.BaseInitialImportService;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.CsvFileRowInfoExtractor;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.MatchResultDetailsByLineIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class PlayerAndResultsInitialImportService extends BaseInitialImportService {

    private final CsvFileRowInfoExtractor rowInfoExtractor;

    private final ClubRepository clubRepository;

    private final ClubMatchingService clubMatchingService;

    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

    private final ClubMemberRepository clubMemberRepository;

    private final PracticionerRepository practicionerRepository;

    private final SeasonPlayerRepository seasonPlayerRepository;

    @Autowired
    public PlayerAndResultsInitialImportService(MatchResultDetailsByLineIterator matchResultDetailsByLineIterator, CsvFileRowInfoExtractor rowInfoExtractor, ClubRepository clubRepository, ClubMatchingService clubMatchingService, ClubMemberRepository clubMemberRepository, PracticionerRepository practicionerRepository, SeasonPlayerRepository seasonPlayerRepository) {
        super(matchResultDetailsByLineIterator);
        this.rowInfoExtractor = rowInfoExtractor;
        this.clubRepository = clubRepository;
        this.clubMatchingService = clubMatchingService;
        this.clubMemberRepository = clubMemberRepository;
        this.practicionerRepository = practicionerRepository;
        this.seasonPlayerRepository = seasonPlayerRepository;
    }

    public void processClubMembersForSeason(String baseSeasonsFolder, String seasonRange) throws IOException {
        resetAndLoadTextFilesForSeason(baseSeasonsFolder, seasonRange);
        importMatchResultsDetailsInfo();
    }

    public void processClubMembersForAllSeasons(String baseSeasonsFolder) throws IOException {
        resetAndLoadTextFilesForAllSeasons(baseSeasonsFolder);
        importMatchResultsDetailsInfo();
    }

    public void importMatchResultsDetailsInfo() {
        List<MatchResultsDetailCsvFileRowInfo> rowInfowsList = fetchCsvRowInfos();
        saveMatchResultsDetailsInfo(rowInfowsList);
    }

    private void saveMatchResultsDetailsInfo(List<MatchResultsDetailCsvFileRowInfo> matchResultsDetailCsvFileRowInfoList) {
        List<Club> allClubsList = clubRepository.findAll();

        matchResultsDetailCsvFileRowInfoList
            .forEach(matchResultsDetailCsvFileRowInfo -> {
                String seasonRange = matchResultsDetailCsvFileRowInfo.fileInfo().season();
                MatchResultsDetailRowInfo rowInfo = rowInfoExtractor.extractMatchDetailsRowInfo(matchResultsDetailCsvFileRowInfo);
                createSeasonPlayerAndResults(rowInfo.acbPlayer(), allClubsList, seasonRange);
                createSeasonPlayerAndResults(rowInfo.xyzPlayer(), allClubsList, seasonRange);
            });
    }

    private void createSeasonPlayerAndResults(PlayerCsvInfo playerInfo, List<Club> allClubsList, String seasonRange) {
        Optional<Club> optInferredClub = inferClubByTeamName(playerInfo.teamName(), allClubsList);
        optInferredClub.ifPresent(club -> createSeasonPlayerAndResultsForClub(club, playerInfo, seasonRange));
    }

    private Optional<Club> inferClubByTeamName(String teamName, List<Club> allClubsList) {
        String normalizedInput = normalize(teamName);
        return allClubsList.stream()
                .min(Comparator.comparingInt(club -> levenshtein.apply(normalizedInput, normalize(club.getName()))));
    }

    private void createSeasonPlayerAndResultsForClub(Club inferredClub, PlayerCsvInfo playerInfo, String seasonRange) {
        Optional<Club> optClub = clubRepository.findByName(inferredClub.getName());
        if (optClub.isPresent()) {

            String[] firstAndSecondNames = splitIntoFirstNameAndSecondName(playerInfo.playerName());
            String firstName = firstAndSecondNames[0];
            String secondName = firstAndSecondNames[1];

            Optional<Practicioner> optPracticionerFromPlayer = practicionerRepository.findByFullName(playerInfo.playerName());

            Practicioner practicionerFromPlayer = optPracticionerFromPlayer.orElseGet(() -> Practicioner.createNew(
                    firstName,
                    secondName,
                    playerInfo.playerName(),
                    null));
            practicionerRepository.save(practicionerFromPlayer);

            Optional<ClubMember> optClubMember = clubMemberRepository.findByPracticionerIdAndClubId(practicionerFromPlayer.getId(), optClub.get().getId());
            ClubMember clubMember = optClubMember.orElseGet(() -> ClubMember.createNew(
                    optClub.get(),
                    practicionerFromPlayer
            ));
            clubMember.addYearRange(seasonRange);
            clubMemberRepository.save(clubMember);

            SeasonPlayer seasonPlayer = seasonPlayerRepository
                    //.findByPracticionerIdClubIdSeason(practicionerFromPlayer.getId(), clubMember.getClub().getId(), seasonRange)
                    .findByPracticionerNameAndClubNameAndSeason(playerInfo.playerName(), optClub.get().getName(), seasonRange)
                    .orElseGet(() -> SeasonPlayer.createNew(
                            clubMember,
                            License.createCatalanaLicenseOf(playerInfo.playerLicense()),
                            seasonRange
                    ));
            seasonPlayerRepository.save(seasonPlayer);
        }
    }

    private String[] splitIntoFirstNameAndSecondName(String input) {
        String[] words = input.split("\\s+");
        List<String> upperWords = new ArrayList<>();
        List<String> lowerWords = new ArrayList<>();

        for (String word : words) {
            if (word.equals(word.toUpperCase())) {
                // Entire word is uppercase
                upperWords.add(word);
            } else {
                // Mixed or lowercase word
                lowerWords.add(word);
            }
        }

        String secondName = String.join(" ", upperWords);
        String firstName = String.join(" ", lowerWords);
        return new String[] {firstName, secondName};
    }

    private String normalize(String s) {
        return s.toLowerCase()
                .replaceAll("[^a-z0-9]", "") // remove spaces/punctuation
                .replace("fc", "");          // remove 'fc' if needed
    }
}

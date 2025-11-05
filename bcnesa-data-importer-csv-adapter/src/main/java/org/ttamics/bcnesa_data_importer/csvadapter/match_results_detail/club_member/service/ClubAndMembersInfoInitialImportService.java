package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club_member.service;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Club;
import org.ttamics.bcnesa_data_importer.core.model.ClubMember;
import org.ttamics.bcnesa_data_importer.core.model.Competition;
import org.ttamics.bcnesa_data_importer.core.model.CompetitionType;
import org.ttamics.bcnesa_data_importer.core.model.License;
import org.ttamics.bcnesa_data_importer.core.model.Practicioner;
import org.ttamics.bcnesa_data_importer.core.model.SeasonPlayer;
import org.ttamics.bcnesa_data_importer.core.model.SeasonPlayerResult;
import org.ttamics.bcnesa_data_importer.core.repository.ClubMemberRepository;
import org.ttamics.bcnesa_data_importer.core.repository.ClubRepository;
import org.ttamics.bcnesa_data_importer.core.repository.PracticionerRepository;
import org.ttamics.bcnesa_data_importer.core.repository.SeasonPlayerRepository;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club_member.model.MatchInfoKey;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.PlayerCsvInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.CsvFileRowInfoExtractor;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.LineByLineInitialImportService;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service.MatchResultDetailsByLineIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ClubAndMembersInfoInitialImportService extends LineByLineInitialImportService {

    private final CsvFileRowInfoExtractor rowInfoExtractor;

    private final ClubRepository clubRepository;

    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

    private final ClubMemberRepository clubMemberRepository;

    private final PracticionerRepository practicionerRepository;

    private final SeasonPlayerRepository seasonPlayerRepository;

    @Autowired
    public ClubAndMembersInfoInitialImportService(MatchResultDetailsByLineIterator matchResultDetailsByLineIterator, CsvFileRowInfoExtractor rowInfoExtractor, ClubRepository clubRepository, ClubMemberRepository clubMemberRepository, PracticionerRepository practicionerRepository, SeasonPlayerRepository seasonPlayerRepository) {
        super(matchResultDetailsByLineIterator);
        this.rowInfoExtractor = rowInfoExtractor;
        this.clubRepository = clubRepository;
        this.clubMemberRepository = clubMemberRepository;
        this.practicionerRepository = practicionerRepository;
        this.seasonPlayerRepository = seasonPlayerRepository;
    }

    public void processForSeason(String baseSeasonsFolder, String seasonRange) throws IOException {
        resetAndLoadTextFilesForSeason(baseSeasonsFolder, seasonRange);
        importMatchResultsDetailsInfo();
    }

    public void processForAllSeasons(String baseSeasonsFolder) throws IOException {
        resetAndLoadTextFilesForAllSeasons(baseSeasonsFolder);
        importMatchResultsDetailsInfo();
    }


    public void importMatchResultsDetailsInfo() {
        List<MatchResultsDetailCsvFileRowInfo> rowInfowsList = fetchCsvRowInfos();
        processMatchResultsDetailsInfo(rowInfowsList);
    }

    private void processMatchResultsDetailsInfo(List<MatchResultsDetailCsvFileRowInfo> matchResultsDetailCsvFileRowInfoList) {
        List<Club> allClubsList = clubRepository.findAll();
        Map<MatchInfoKey, List<SeasonPlayerResult>> mapOfMatchesList = new HashMap<>();

        matchResultsDetailCsvFileRowInfoList.parallelStream().forEach(matchResultsDetailCsvFileRowInfo -> {
            String seasonRange = matchResultsDetailCsvFileRowInfo.fileInfo().season();
            CompetitionType competitionType = matchResultsDetailCsvFileRowInfo.fileInfo().competitionType();
            Competition competition = matchResultsDetailCsvFileRowInfo.fileInfo().competition();
            String jornada = matchResultsDetailCsvFileRowInfo.fileInfo().jornada();
            String grupo = matchResultsDetailCsvFileRowInfo.fileInfo().group();


            MatchResultsDetailRowInfo rowInfo = rowInfoExtractor.extractMatchDetailsRowInfo(matchResultsDetailCsvFileRowInfo);
            String gameId = rowInfo.acbPlayer().playerLetter() + "-" + rowInfo.xyzPlayer().playerLetter();
            processInfoForPlayer(seasonRange, competitionType, competition, jornada, grupo, rowInfo.acbPlayer(), gameId, allClubsList);
            processInfoForPlayer(seasonRange, competitionType, competition, jornada, grupo, rowInfo.xyzPlayer(), gameId, allClubsList);
        });
    }

    private void processInfoForPlayer(String seasonRange, CompetitionType competitionType, Competition competition, String jornada, String grupo, PlayerCsvInfo playerInfo, String gameId, List<Club> allClubsList) {
        Optional<Club> optInferredClub = inferClubByTeamName(playerInfo.teamName(), allClubsList);
        SeasonPlayerResult seasonPlayerResult = null;
        if (optInferredClub.isPresent()) {
            Club inferredClub = optInferredClub.get();
            Optional<Club> optClub = clubRepository.findByName(inferredClub.getName());
            if (optClub.isPresent()) {
                Club club = optClub.get();

                Practicioner practicioner = getOrCreatePracticionerFromPlayerInfo(playerInfo);
                ClubMember clubMember = getOrCreateClubMember(club, practicioner, seasonRange);
                SeasonPlayer seasonPlayer = getOrCreateSeasonPlayer(playerInfo, club, clubMember, seasonRange);

            }
        }
    }

    private Practicioner getOrCreatePracticionerFromPlayerInfo(PlayerCsvInfo playerInfo) {
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
        return practicionerFromPlayer;
    }

    private ClubMember getOrCreateClubMember(Club club, Practicioner practicioner, String seasonRange) {
        Optional<ClubMember> optClubMember = clubMemberRepository.findByPracticionerIdAndClubId(practicioner.getId(), club.getId());
        ClubMember clubMember = optClubMember.orElseGet(() -> ClubMember.createNew(
                club,
                practicioner
        ));
        clubMember.addYearRange(seasonRange);
        clubMemberRepository.save(clubMember);
        return clubMember;
    }

    private SeasonPlayer getOrCreateSeasonPlayer(PlayerCsvInfo playerInfo, Club club, ClubMember clubMember, String seasonRange) {
        SeasonPlayer seasonPlayer = seasonPlayerRepository
                .findByPracticionerNameAndClubNameAndSeason(playerInfo.playerName(), club.getName(), seasonRange)
                .orElseGet(() -> SeasonPlayer.createNew(
                        clubMember,
                        License.createCatalanaLicenseOf(playerInfo.playerLicense()),
                        seasonRange
                ));
        seasonPlayerRepository.save(seasonPlayer);
        return seasonPlayer;
    }

    private Optional<Club> inferClubByTeamName(String teamName, List<Club> allClubsList) {
        String normalizedInput = normalize(teamName);
        return allClubsList.stream()
                .min(Comparator.comparingInt(club -> levenshtein.apply(normalizedInput, normalize(club.getName()))));
    }
    private String normalize(String s) {
        return s.toLowerCase()
                .replaceAll("[^a-z0-9]", "") // remove spaces/punctuation
                .replace("fc", "");          // remove 'fc' if needed
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
}

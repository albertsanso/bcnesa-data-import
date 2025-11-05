package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.repository.ClubRepository;
import org.ttamics.bcnesa_data_importer.core.repository.SeasonPlayerRepository;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club.service.ClubNameGrouppingService;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.PlayerCsvInfo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class ParseMatchResultsDetailsService {

    private static final Pattern JORNADA_AND_GRUP_FILENAME_PATTERN = Pattern.compile("jornada(\\d+)-g(\\d+)\\.csv");

    private final ClubRepository clubRepository;
    private final SeasonPlayerRepository seasonPlayerRepository;
    private final CsvRepositoryFinderService csvRepositoryFinderService;
    private final ClubNameGrouppingService clubNameGrouppingService;

    @Autowired
    public ParseMatchResultsDetailsService(ClubRepository clubRepository, SeasonPlayerRepository seasonPlayerRepository, CsvRepositoryFinderService csvRepositoryFinderService, ClubNameGrouppingService clubNameGrouppingService) {
        this.clubRepository = clubRepository;
        this.seasonPlayerRepository = seasonPlayerRepository;
        this.csvRepositoryFinderService = csvRepositoryFinderService;
        this.clubNameGrouppingService = clubNameGrouppingService;
    }
/*
    public void processMatchesResultsDetailsCsv(String filePath) {
        List<String> clubNamesList = List.of();
        readBCNesaPlayersFromMatches(filePath).forEach(matchResultsDetailCsvInfo -> {
            List<String> clubNamesInRow = processMatchesResultsDetailsCsvRow(matchResultsDetailCsvInfo);
            clubNamesList.addAll(clubNamesInRow);

        });

        Map<String, List<String>> stringListMap = clubNameGrouppingService.groupByCommonRoot(clubNamesList);
        System.out.println();

    }
*/
    private CSVReader getReaderFromFilePath(String filePath) throws FileNotFoundException {
        return new CSVReaderBuilder(new FileReader(filePath))
                .withSkipLines(1)
                .build();
    }

    private List<MatchResultsDetailRowInfo> readBCNesaPlayersFromMatches(String filePath) {

        List<MatchResultsDetailRowInfo>  matchResultsDetailsInfo = new ArrayList<>();
        try (CSVReader csvReader = getReaderFromFilePath(filePath)) {
            String[] csvLine;
            while ((csvLine = csvReader.readNext()) != null) {
                matchResultsDetailsInfo.add(parseMatchDetails(csvLine));
            }
        } catch (IOException | CsvValidationException e) {
            throw new IllegalStateException(e);
        }
        // read Year, Club, License, Player
        return matchResultsDetailsInfo;
    }

    private List<String> processMatchesResultsDetailsCsvRow(MatchResultsDetailRowInfo matchResultDetails) {

        String clubNameAbc = processTeamNameForClubName(matchResultDetails.acbPlayer().teamName());
        String clubNameXyz = processTeamNameForClubName(matchResultDetails.xyzPlayer().teamName());
        return List.of(clubNameAbc, clubNameXyz);

        /*
        Player abcPlayer = Player.createNew(matchResultDetails.getAcbPlayer().getPlayerName());
        Player xyzPlayer = Player.createNew(matchResultDetails.getXyzPlayer().getPlayerName());
        createUserIfDoesntExistYet(abcPlayer);
        createUserIfDoesntExistYet(xyzPlayer);

        Club abcClub = Club.createNew(processTeamNameForClubName(matchResultDetails.getAbcTeamName()));
        Club xyzClub = Club.createNew(processTeamNameForClubName(matchResultDetails.getXyzTeamName()));
        createClubIfDoesntExistYet(abcClub);
        createClubIfDoesntExistYet(xyzClub);

        Team abcTeam = Team.createNew(matchResultDetails.getAbcTeamName());
        Team xyzTeam = Team.createNew(matchResultDetails.getXyzTeamName());
        createTeamIfDoesntExistYet(abcTeam);
        createTeamIfDoesntExistYet(xyzTeam);
         */
    }

    private MatchResultsDetailRowInfo parseMatchDetails(String[] csvLine) {
        PlayerCsvInfo abcPlayer = null;//parsePlayerABC(csvLine);
        PlayerCsvInfo xyzPlayer = null;//parsePlayerXYZ(csvLine);
        return new MatchResultsDetailRowInfo(abcPlayer, xyzPlayer);
    }

    private String processTeamNameForClubName(String teamName) {
        String regex = "(?:''|\"\")([a-zA-Z0-9])(?:''|\"\")";
        return teamName.replaceAll(regex, "").trim();
    }
}

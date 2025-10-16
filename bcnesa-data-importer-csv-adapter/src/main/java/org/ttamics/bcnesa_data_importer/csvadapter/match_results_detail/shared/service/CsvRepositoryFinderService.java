package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service;

import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Competition;
import org.ttamics.bcnesa_data_importer.core.model.CompetitionType;
import org.ttamics.bcnesa_data_importer.core.service.SeasonRangeValidator;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.CompetitionFolderInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.CompetitionTypeFolderInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.SeasonFolderInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CsvRepositoryFinderService {

    public Optional<List<CompetitionFolderInfo>> findCompetitionFoldersFrom(String baseFolder) {
        return Optional.of(Arrays.stream(Competition.values())
                .map(competition ->
                        new CompetitionFolderInfo(
                                competition,
                                buildCompetitionFolder(competition, baseFolder).toString()
                        )
                )
                .filter(competitionFolderInfo -> Files.isDirectory(Path.of(competitionFolderInfo.folder())))
                .toList()
        ).filter(l -> !l.isEmpty());
    }

    private Path buildCompetitionFolder(Competition competition, String baseFolder) {
        String[] split = competition.getCompetitionLevel().split(",");
        String folderName = split[0];

        if (split.length == 2) {
            folderName += split[1].toLowerCase();
        }

        return Path.of(baseFolder).resolve(folderName);
    }

    public Optional<List<SeasonFolderInfo>> findAllSeasonsFoldersFrom(String baseFolder) throws IOException {

        Path baseFolderPath = Path.of(baseFolder);
        return Optional.of(Files.list(baseFolderPath)
                .filter(Files::isDirectory)
                .filter(seasonPath -> SeasonRangeValidator.isValidYearRange(seasonPath.getFileName().toString()))
                .map(seasonPath -> new SeasonFolderInfo(seasonPath.getFileName().toString(), seasonPath.toString()))
                .toList()
        ).filter(l -> !l.isEmpty());
    }

    public Optional<List<CompetitionTypeFolderInfo>> findCompetitionTypeFoldersFrom(String baseFolder) throws IOException {
        Path baseFolderPath = Path.of(baseFolder);
        return Optional.of(Files.list(baseFolderPath)
                .filter(Files::isDirectory)
                .filter(competitionTypePath ->
                        Arrays.stream(CompetitionType.values())
                                .anyMatch(competitionType ->
                                        competitionTypePath.endsWith(Path.of(competitionType.getValue()))))
                .map(competitionTypePath -> new CompetitionTypeFolderInfo(
                        CompetitionType.fromValue(competitionTypePath.getFileName().toString()),
                        competitionTypePath.toString()
                ))
                .toList()
        ).filter(l -> !l.isEmpty());
    }
}

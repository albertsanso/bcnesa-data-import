package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.CompetitionFolderInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.CompetitionTypeFolderInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.SeasonFolderInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MatchResultDetailsByLineIterator implements Iterator<MatchResultsDetailCsvFileRowInfo>, AutoCloseable {

    private static final Pattern JORNADA_AND_GRUP_FILENAME_PATTERN = Pattern.compile("jornada(\\d+)-g(\\d+)\\.csv");

    private final CsvRepositoryFinderService csvRepositoryFinderService;

    private Queue<MatchResultsDetailCsvFileInfo> fileQueue = new LinkedList<>();
    private CSVReader currentReader;
    private String[] nextLine;
    private MatchResultsDetailCsvFileInfo currentInfo;

    @Autowired
    public MatchResultDetailsByLineIterator(CsvRepositoryFinderService csvRepositoryFinderService) {
        this.csvRepositoryFinderService = csvRepositoryFinderService;
    }

    public void resetAndLoadTextFilesForSeason(File dir, String seasonRange) throws IOException {
        fileQueue.clear();
        if (currentReader != null) currentReader.close();
        loadTextFilesForSeason(dir, seasonRange);
        advanceReader();
    }

    public void resetAndLoadTextFilesForAllSeasons(File dir) throws IOException {
        fileQueue.clear();
        if (currentReader != null) currentReader.close();
        loadTextFilesForAllSeasons(dir);
        advanceReader();
    }

    private void loadTextFilesForSeason(File baseNatchesDetailsCsvFilesFolder, String seasonRange) throws IOException {
        processMatchesDetailsForSeason(baseNatchesDetailsCsvFilesFolder.toString(), seasonRange);
    }

    private void loadTextFilesForAllSeasons(File baseNatchesDetailsCsvFilesFolder) throws IOException {
        processMatchesDetailsForAllSeasons(baseNatchesDetailsCsvFilesFolder.toString());
    }

    private void processMatchesDetailsForAllSeasons(String baseNatchesDetailsCsvFilesFolder) throws IOException {
        csvRepositoryFinderService.findAllSeasonsFoldersFrom(baseNatchesDetailsCsvFilesFolder)
            .ifPresent(seasonFolderInfos ->
                    seasonFolderInfos.stream()
                            .forEach(this::processSeasonFolder)
            );
    }

    private void processMatchesDetailsForSeason(String baseNatchesDetailsCsvFilesFolder, String seasonRange) throws IOException {
        csvRepositoryFinderService.findAllSeasonsFoldersFrom(baseNatchesDetailsCsvFilesFolder)
            .ifPresent(seasonFolderInfos ->
                seasonFolderInfos.stream()
                    .filter(seasonFolderInfo -> seasonFolderInfo.season().equals(seasonRange))
                    .forEach(this::processSeasonFolder)
            );
    }

    private void processSeasonFolder(SeasonFolderInfo seasonFolderInfo) {

        Optional<List<CompetitionTypeFolderInfo>> optCompetitionTypeFolders;
        try {
            optCompetitionTypeFolders = csvRepositoryFinderService.findCompetitionTypeFoldersFrom(seasonFolderInfo.folder());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        optCompetitionTypeFolders.ifPresent(competitionTypeFolderInfos -> {
            competitionTypeFolderInfos.forEach(competitionTypeFolderInfo ->  processCompetitionTypeFolder(competitionTypeFolderInfo, seasonFolderInfo));
        });
    }

    private void processCompetitionTypeFolder(CompetitionTypeFolderInfo competitionTypeFolderInfo, SeasonFolderInfo seasonFolderInfo) {
        csvRepositoryFinderService.findCompetitionFoldersFrom(competitionTypeFolderInfo.folder()).ifPresent(competitionFolderInfos -> {
            competitionFolderInfos.forEach(competitionFolderInfo -> processCompetitionFolder(competitionFolderInfo, seasonFolderInfo, competitionTypeFolderInfo));
        });
    }

    private void processCompetitionFolder(CompetitionFolderInfo competitionFolderInfo, SeasonFolderInfo seasonFolderInfo, CompetitionTypeFolderInfo competitionTypeFolderInfo) {
        try {
            Files.list(Path.of(competitionFolderInfo.folder())).forEach(csvFilePath -> {
                Matcher matcher = JORNADA_AND_GRUP_FILENAME_PATTERN.matcher(csvFilePath.getFileName().toString());

                if (matcher.matches()) {
                    String jornadaNumber = matcher.group(1);
                    String groupNumber = matcher.group(2);

                    MatchResultsDetailCsvFileInfo info = new MatchResultsDetailCsvFileInfo(
                            csvFilePath,
                            seasonFolderInfo.season(),
                            competitionTypeFolderInfo.competitionType(),
                            competitionFolderInfo.competition(),
                            jornadaNumber,
                            groupNumber);
                    fileQueue.add(info);
                } else {
                    throw new RuntimeException("Wrong file name format for match results details: %s".formatted(csvFilePath));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void advanceReader() {
        try {
            while (currentReader == null || (nextLine = currentReader.readNext()) == null) {
                if (currentReader != null) currentReader.close();
                if (fileQueue.isEmpty()) {
                    nextLine = null;
                    return;
                }
                currentInfo = fileQueue.poll();
                currentReader = getReaderFromBufferedReader(new BufferedReader(new FileReader(currentInfo.csvFilepath().toFile())));

            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private CSVReader getReaderFromBufferedReader(BufferedReader bufferedReader) throws FileNotFoundException {
        return new CSVReaderBuilder(bufferedReader)
                .withSkipLines(1)
                .build();
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public MatchResultsDetailCsvFileRowInfo next() {
        if (!hasNext()) throw new NoSuchElementException();
        String[] lineToReturn = nextLine;
        MatchResultsDetailCsvFileRowInfo rowInfoToReturn = new MatchResultsDetailCsvFileRowInfo(currentInfo, lineToReturn);
        advanceReader();
        return rowInfoToReturn;
    }

    public void close() {
        try {
            if (currentReader != null) currentReader.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

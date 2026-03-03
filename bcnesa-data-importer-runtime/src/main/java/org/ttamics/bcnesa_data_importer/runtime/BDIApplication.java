package org.ttamics.bcnesa_data_importer.runtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club.service.ClubInitialImportService;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club_member.service.PlayerAndResultsInitialImportService;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {
        "org.ttamics.bcnesa_data_importer.core",
        "org.ttamics.bcnesa_data_importer.csvadapter",
        "org.ttamics.bcnesa_data_importer.jpa"
})
@EnableJpaRepositories(basePackages = "org.ttamics.bcnesa_data_importer.jpa")
@EntityScan(basePackages = "org.ttamics.bcnesa_data_importer.jpa")
public class BDIApplication implements CommandLineRunner {

    @Autowired
    private ClubInitialImportService clubInitialImportService;

    @Autowired
    private PlayerAndResultsInitialImportService playerAndResultsInitialImportService;

    public static void main(String[] args) {
        SpringApplication.run(BDIApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String baseFolder = "C:\\git\\bcnesa-data-csv\\resources\\matches-results-details\\csv";
        //String baseFolder = "C:\\git\\folder-test\\test-2";

        clubInitialImportService.processClubNamesForAllSeasons(baseFolder);
        //playerAndResultsInitialImportService.processForSeason(baseFolder, "2024-2025");

        //String baseFolder = "D:\\data\\resources\\test-1";
        processMatchesAndResultsInfoForFolderAndBySeason(baseFolder, "2019-2020");

        System.out.println("FINISH.");
    }

    private void processClubAndMembersInfoForFolderAndAllSeasons(String baseFolder) throws IOException {
        playerAndResultsInitialImportService.processForAllSeasons(baseFolder);
    }

    private void processClubAndMembersInfoForFolderAndBySeason(String baseFolder, String season) throws IOException {
        playerAndResultsInitialImportService.processForSeason(baseFolder, season);
    }

    private void processMatchesAndResultsInfoForFolderAndAllSeasons(String baseFolder) throws IOException {
        playerAndResultsInitialImportService.processForAllSeasons(baseFolder);
    }

    private void processMatchesAndResultsInfoForFolderAndBySeason(String baseFolder, String season) throws IOException {
        playerAndResultsInitialImportService.processForSeason(baseFolder, season);
    }
}



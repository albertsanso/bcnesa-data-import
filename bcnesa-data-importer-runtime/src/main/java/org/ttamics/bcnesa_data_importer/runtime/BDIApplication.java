package org.ttamics.bcnesa_data_importer.runtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.ttamics.bcnesa_data_importer.core.model.Competition;
import org.ttamics.bcnesa_data_importer.core.model.CompetitionType;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club_member.service.PlayerAndResultsInitialImportService;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.shared.model.MatchResultsDetailCsvFileRowInfo;
import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club.service.ClubInitialImportService;

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

        clubInitialImportService.processClubNamesForAllSeasons(baseFolder);

        playerAndResultsInitialImportService.processClubMembersForSeason(baseFolder, "2024-2025");
        //clubMemberInitialImportService.processClubMembersForAllSeasons(baseFolder);
    }

    private static void processRowInfo(MatchResultsDetailCsvFileRowInfo rowInfo) {
        String season = rowInfo.fileInfo().season();
        CompetitionType competitionType = rowInfo.fileInfo().competitionType();
        Competition competition = rowInfo.fileInfo().competition();
        String jornada = rowInfo.fileInfo().jornada();
        String grup = rowInfo.fileInfo().group();
        String[] rowStringData = rowInfo.rowInfo();
        System.out.println();
    }
}



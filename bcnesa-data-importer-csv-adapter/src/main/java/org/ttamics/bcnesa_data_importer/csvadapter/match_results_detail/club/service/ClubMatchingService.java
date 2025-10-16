package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club.service;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Club;
import org.ttamics.bcnesa_data_importer.core.repository.ClubRepository;

import java.util.Comparator;

@Component
public class ClubMatchingService {

    private final ClubRepository clubRepository;
    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

    public ClubMatchingService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public Club findClosestMatch(String inputName) {
        String normalizedInput = normalize(inputName);

        return clubRepository.findAll().stream()
                .min(Comparator.comparingInt(club -> levenshtein.apply(normalizedInput, normalize(club.getName()))))
                .orElse(null);
    }

    private String normalize(String s) {
        return s.toLowerCase()
                .replaceAll("[^a-z0-9]", "") // remove spaces/punctuation
                .replace("fc", "");          // remove 'fc' if needed
    }
}
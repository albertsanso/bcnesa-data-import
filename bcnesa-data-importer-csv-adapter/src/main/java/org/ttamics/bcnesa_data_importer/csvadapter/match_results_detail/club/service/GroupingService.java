package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club.service;

import org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club.model.ClubNameAndYearInfo;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupingService <T> {

    private static final double MERGE_THRESHOLD = 0.7;
    private final Function<T, String> nameExtractor;
    private final Function<T, String> yearExtractor;

    public GroupingService(Function<T, String> nameExtractor, Function<T, String> yearExtractor) {
        this.nameExtractor = nameExtractor;
        this.yearExtractor = yearExtractor;
    }

    // ---------------- CORE GROUPING LOGIC ----------------
    public Map<String, List<String>> groupByCommonRoot(List<T> items) {
        Map<String, List<String>> groups = new LinkedHashMap<>();
        Map<String, Set<String>> groupsAndYears = new LinkedHashMap<>();
        Map<String, List<String>> outputClubNamesAndYears = new LinkedHashMap<>();

        for (T item : items) {
            String itemName = nameExtractor.apply(item);
            String yearRange = yearExtractor.apply(item);

            String normalizedItem = normalize(itemName);
            String bestRoot = null;
            double bestScore = 0.0;

            for (String existingRoot : groups.keySet()) {
                double similarity = levenshteinSimilarity(normalize(existingRoot), normalizedItem);
                double adaptiveThreshold = getAdaptiveThreshold(existingRoot, itemName);

                if (similarity >= adaptiveThreshold && similarity > bestScore) {
                    bestRoot = existingRoot;
                    bestScore = similarity;
                }
            }

            if (bestRoot != null) {
                groups.get(bestRoot).add(itemName);
                groupsAndYears.get(bestRoot).add(yearRange);
            } else {
                groups.put(itemName, new ArrayList<>(Collections.singletonList(itemName)));
                groupsAndYears.put(itemName, new HashSet<>(Collections.singletonList(yearRange)));
            }
        }

        // Merge similar groups
        Map<String, List<String>> mergedGroupsMap = mergeSimilarGroups(groups);

        // Combine year ranges for merged roots
        for (Map.Entry<String, List<String>> e : mergedGroupsMap.entrySet()) {
            String clubRoot = e.getKey();
            Set<String> rangesSet = e.getValue().stream()
                    .map(name -> groupsAndYears.getOrDefault(name, Set.of()))
                    .flatMap(Set::stream)
                    .collect(Collectors.toCollection(TreeSet::new));

            outputClubNamesAndYears.put(clubRoot, new ArrayList<>(rangesSet));
        }

        return outputClubNamesAndYears;
    }

    // ---------------- ADAPTIVE THRESHOLD ----------------
    private static double getAdaptiveThreshold(String a, String b) {
        int avgLength = (a.length() + b.length()) / 2;
        if (avgLength < 15) return 0.85;
        if (avgLength < 30) return 0.75;
        if (avgLength < 50) return 0.65;
        return 0.55;
    }

    // ---------------- LEVENSHTEIN SIMILARITY ----------------
    private static double levenshteinSimilarity(String s1, String s2) {
        int dist = levenshteinDistance(s1, s2);
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 1.0;
        return 1.0 - ((double) dist / maxLen);
    }

    private static int levenshteinDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int j = 0; j <= s2.length(); j++) costs[j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= s2.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        s1.charAt(i - 1) == s2.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[s2.length()];
    }

    // ---------------- GROUP MERGING / ROOT CLEANING ----------------
    private static Map<String, List<String>> mergeSimilarGroups(Map<String, List<String>> groups) {
        Map<String, List<String>> merged = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> e : groups.entrySet()) {
            String root = cleanRoot(e.getKey());
            Optional<String> match = merged.keySet().stream()
                    .filter(r -> levenshteinSimilarity(normalize(r), normalize(root)) > MERGE_THRESHOLD)
                    .findFirst();
            if (match.isPresent()) {
                merged.get(match.get()).addAll(e.getValue());
            } else {
                merged.put(root, new ArrayList<>(e.getValue()));
            }
        }
        return merged;
    }

    private static String normalize(String s) {
        String noAccent = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noAccent.toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }

    private static String cleanRoot(String s) {
        return s.replaceAll("\\s+-\\s*.*$", "").trim();
    }

    public static void main(String[] args) {
        List<ClubNameAndYearInfo> clubs = List.of(
                new ClubNameAndYearInfo("Manchester United", "1990-2000"),
                new ClubNameAndYearInfo("Man United", "2000-2010"),
                new ClubNameAndYearInfo("Manchester Utd FC", "2010-2020")
        );

        GroupingService<ClubNameAndYearInfo> service =
                new GroupingService<>(ClubNameAndYearInfo::clubName, ClubNameAndYearInfo::yearRange);

        Map<String, List<String>> grouped = service.groupByCommonRoot(clubs);

        grouped.forEach((club, years) ->
                System.out.println(club + " -> " + years)
        );
    }

}
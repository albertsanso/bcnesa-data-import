package org.ttamics.bcnesa_data_importer.runtime;

import java.text.Normalizer;
import java.util.*;

public class GroupByFuzzyRoot {

    private static final double MERGE_THRESHOLD = 0.7;

    public static void main(String[] args) {
        List<String> items = Arrays.asList(
                "CTT SANT QUIRZE DEL VALLÈS - Sènior",
                "CTT ST QUIRZE DEL VALLÈS - Sènior A",
                "CTT SANT QUIRZE DEL VALLÈS - Sènior B",
                "CTT SANT QUIRZE DEL VALLÈS - Vet A",
                "CTT ST QUIRZE DEL VALLÈS - Vet B",
                "CTT SANT QUIRZE DEL VALLÈS - Vet C",
                "CTT ST Q DEL VALLÈS"
        );

        Map<String, List<String>> groups = groupByCommonRoot(items);

        groups.forEach((root, group) -> {
            System.out.println(root + " → " + group);
        });
    }

    // ---------------- CORE GROUPING LOGIC ----------------
    private static Map<String, List<String>> groupByCommonRoot(List<String> items) {
        Map<String, List<String>> groups = new LinkedHashMap<>();

        for (String item : items) {
            String normalizedItem = normalize(item);
            String bestRoot = null;
            double bestScore = 0.0;

            for (String existingRoot : groups.keySet()) {
                double similarity = levenshteinSimilarity(normalize(existingRoot), normalizedItem);
                double adaptiveThreshold = getAdaptiveThreshold(existingRoot, item);

                if (similarity >= adaptiveThreshold && similarity > bestScore) {
                    bestRoot = existingRoot;
                    bestScore = similarity;
                }
            }

            if (bestRoot != null) {
                groups.get(bestRoot).add(item);
            } else {
                groups.put(item, new ArrayList<>(Collections.singletonList(item)));
            }
        }

        // Optionally refine roots by picking the common core phrase
        return mergeSimilarGroups(groups);
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
        // Remove accents and lowercase
        String noAccent = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noAccent.toLowerCase().trim().replaceAll("\\s+", " ");
    }

    private static String cleanRoot(String s) {
        return s.replaceAll("\\s+-\\s*.*$", "").trim();
    }
}

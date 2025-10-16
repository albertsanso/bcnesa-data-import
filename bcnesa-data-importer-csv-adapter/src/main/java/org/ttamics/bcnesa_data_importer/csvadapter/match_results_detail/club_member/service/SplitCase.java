package org.ttamics.bcnesa_data_importer.csvadapter.match_results_detail.club_member.service;

import java.util.ArrayList;
import java.util.List;

public class SplitCase {
    public static void main(String[] args) {
        String input = "CATALÀ Angel Maria";

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

        String upper = String.join(" ", upperWords);
        String lower = String.join(" ", lowerWords);

        System.out.println("Uppercase: " + upper);
        System.out.println("Lowercase: " + lower);
    }
}
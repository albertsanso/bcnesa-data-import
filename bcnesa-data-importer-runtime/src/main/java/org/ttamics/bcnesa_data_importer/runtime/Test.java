package org.ttamics.bcnesa_data_importer.runtime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("\"(.)\"");
        //Pattern.compile("\"(.)\"");
        String teamName ="CN SABADELL A";
        Matcher matcher = pattern.matcher(teamName);
        if (matcher.find()) {
            System.out.println("HHH");
        }
    }
}

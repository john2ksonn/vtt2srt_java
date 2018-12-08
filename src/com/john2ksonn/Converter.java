package com.john2ksonn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Converter {
    public ArrayList<String> convert(String uri) {
        int counter = 1;
        boolean cue_flag = false;
        StringBuilder cue = new StringBuilder();
        ArrayList<String> output = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(uri));
            while (br.ready()) {
                String line = br.readLine();
                if (isTimingLine(line) && !cue_flag) {
                    cue_flag = true;
                    output.add(String.valueOf(counter));
                    counter++;
                    output.add(cleanTimingLine(line));
                } else if (cue_flag) {
                    if (isTimingLine(line) || line.isEmpty()) {
                        for (String cue_line : cue.toString().split("\n"))
                            output.add(cleanCueLine(cue_line));
                        output.add("");
                        cue_flag = false;
                        cue = new StringBuilder();
                    } else
                        cue.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    private String cleanCueLine(String cue) {
        boolean tag_flag = false;
        StringBuilder clean_cue = new StringBuilder();
        for (char ch : cue.toCharArray()) {
            if (ch == '<')
                tag_flag = true;
            else if (ch == '>')
                tag_flag = false;
            else if (!tag_flag)
                clean_cue.append(ch);
        }
        return clean_cue.toString();
    }

    private String cleanTimingLine(String line) {
        StringBuilder clean_line = new StringBuilder();
        for (char ch : line.trim().toCharArray()) {
            if (Character.isAlphabetic(ch))
                break;
            clean_line.append(ch);
        }
        return clean_line.toString().trim().replace(".", ",");
    }

    private boolean isTimingLine(String line) {
        char[] chars = line.toCharArray();
        if (line.length() >= 5)
            return Character.isDigit(chars[0]) && Character.isDigit(chars[1]) && chars[2] == ':'
                    && chars[5] == ':' && line.contains("-->");
        return false;
    }

}

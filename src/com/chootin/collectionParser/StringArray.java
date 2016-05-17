package com.chootin.collectionParser;

import java.util.ArrayList;

/**
 * Created by AlecT on 17-May-16.
 */
public class StringArray {
    public static String[] toArray(String arrayString) {
        char[] temp = arrayString.toCharArray();
        ArrayList<String> stringStore = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        boolean quotesOpen = false;
        boolean subArray = false;
        boolean jsonOpen = false;
        int depth = 0;
        int jsonDepth = 0;

        for (char c : temp) {
            if (c == '"') {
                quotesOpen = !quotesOpen;
            } else if (!quotesOpen && !subArray && c == '{') {
                jsonDepth++;
                jsonOpen = true;
            } else if (!quotesOpen && !subArray && c == '}') {
                jsonDepth--;
                if (jsonDepth == 0) {
                    jsonOpen = false;
                }
            } else if (!quotesOpen && !subArray && !jsonOpen && c == ',') {
                stringStore.add(builder.toString());
                builder.delete(0, builder.length());
                continue;
            } else if (!quotesOpen && !subArray && !jsonOpen && (c == ' ' || c == '\n')) {
                continue;
            } else if (!quotesOpen && c == '[') {
                depth++;
                if (depth > 1) {
                    subArray = true;
                } else {
                    continue;
                }
            } else if (!quotesOpen && c == ']') {
                depth--;
                if (depth == 0) {
                    stringStore.add(builder.toString());
                    break;
                } else if (depth == 1) {
                    subArray = false;
                } else {
                    continue;
                }
            }

            builder.append(c);
        }
        return stringStore.toArray(new String[stringStore.size()]);
    }
}

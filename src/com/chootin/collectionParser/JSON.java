package com.chootin.collectionParser;

import java.util.HashMap;

import static com.chootin.collectionParser.StringArray.*;

/**
 * Created by AlecT on 17-May-16.
 */

public class JSON {

    //Testing main
    /*public static void main(String[] args) {
        String string = "{\"testChallenge\":[{\"id\":1,\"engname\":\"a, i, u, e, o\",\"japname\":\"あ、　い、　う、　え、　お\",\"path\":\"Kana\",\"gametype\":\"button\",\"numans\":\"4\",\"repeat\":1,\"numrepeats\":20,\"quetype\":\"Kana\",\"anstype\":\"English\",\"fonts\":\"1\",\"selectortype\":\"Kana\",\"prereq\":null}]}";
        JSON json = new JSON(string);
        System.out.println(json.getStringArray("testChallenge")[0]);
        System.out.println(new JSON(json.getStringArray("testChallenge")[0]).get("engname"));

    }*/

    private HashMap<String, String> data = new HashMap<>();
    private String jsonString;

    public JSON(String jsonString) {
        this.jsonString = jsonString;
        char[] jsonChars = jsonString.toCharArray();
        StringBuilder builder = new StringBuilder();
        String temp = null;
        boolean quotesOpen = false;
        boolean arrayOpen = false;
        boolean subjson = false;
        int depth = 0;
        int arrayDepth = 0;
        for (char c : jsonChars) {
            if (!quotesOpen && c == '{') {
                depth++;
                if (depth == 1) {
                    continue;
                } else if (depth > 1) {
                    subjson = true;
                }
            } else if (!quotesOpen && c == '}') {
                depth--;
                if (depth == 0) {
                    data.put(temp, builder.toString());
                    break;
                } else if (depth == 1) {
                    subjson = false;
                }
            } else if (!subjson) {
                if (c == '"') {
                    quotesOpen = !quotesOpen;
                    continue;
                } else if (!quotesOpen && c == '[') {
                    arrayOpen = true;
                    arrayDepth++;
                } else if (!quotesOpen && c == ']') {
                    arrayDepth--;
                    if (arrayDepth == 0) {
                        arrayOpen = false;
                    }
                }
                if (!arrayOpen && !quotesOpen) {
                    if (!quotesOpen && c == ':') {
                        temp = builder.toString();
                        builder.delete(0, builder.length());
                        continue;
                    } else if (!quotesOpen && c == ',') {
                        data.put(temp, builder.toString());
                        builder.delete(0, builder.length());
                        continue;
                    } else if ((c == '\n' || c == ' ') && !quotesOpen) {
                        continue;
                    }
                }
            }
            builder.append(c);
        }
    }

    public String get(String key) {
        return data.get(key);
    }

    public String getString(String key) {
        return data.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(data.get(key));
    }

    public JSON getJSON(String key) {
        return new JSON(data.get(key));
    }

    public String[] getStringArray(String key) {
        return toArray(data.get(key));
    }

    @Override
    public String toString() {
        return jsonString;
    }
}

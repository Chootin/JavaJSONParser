package com.chootin.collectionParser;

import java.util.HashMap;

import static com.chootin.collectionParser.StringArray.*;

/**
 * Created by AlecT on 17-May-16.
 */

public class JSON {

    /*
    //Testing main
    public static void main(String[] args) {
        String string = "{\"property\": \"value\", \"property2\": {subjsonproperty: \"value\", mmmm: \n" +
                "3}, property3: [[value, value2], {fruit: \n" +
                "banana, fruit2: bigbanana}]}";
        JSON json = new JSON(string);
        System.out.println(json.get("property").equals("value"));
        JSON subJSON = json.getJSON("property2");
        System.out.println(subJSON.get("subjsonproperty").equals("value"));
        System.out.println(subJSON.getInt("mmmm") == 3);
        System.out.println(json.toString().equals(string));
        String[] property3 = json.getStringArray("property3");
        System.out.println(property3[0].equals("[value, value2]"));
        System.out.println(toArray(property3[0])[1].equals("value2"));
        System.out.println(property3[1]);
        System.out.println(new JSON(property3[1]).get("fruit2").equals("bigbanana"));
    }
    */

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
                } else {
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
                if (!arrayOpen) {
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
        for (String key : data.keySet()) {
            System.out.println("Processed output - " + key + ": " + data.get(key));
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

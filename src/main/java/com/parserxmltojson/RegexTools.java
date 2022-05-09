package com.parserxmltojson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTools {

    public static Pattern xmlVersion = Pattern.compile("<\\?.*\\?>");
    public static Pattern xmlSingleLineComment = Pattern.compile("<!--.*-->");
    public static Pattern xmlOpeningTag = Pattern.compile("(?<=<)[A-Z]{1,}(?=>)");
    public static Pattern xmlContent = Pattern.compile("^(\\w|\\d|\\.|\\,|\\s|')+(?=<\\/[A-Z]{1,}>)");
    //public static Pattern xmlContent = Pattern.compile("^([^<>])+(?=<\\/[A-Z]{1,}>)");
    public static Pattern xmlClosingTag = Pattern.compile("(?<=<\\/)[A-Z]{1,}(?=>)");

    public static TagItem GetXmlMatch(String line) {

        if (xmlContent.matcher(line).find()) {
            Matcher matcher = xmlContent.matcher(line);
            matcher.find();
            return new TagItem(matcher.group(), "content");
        } else {
            String tag = "";

            for (int i = 0; i < line.length(); i++) {
                tag += line.charAt(i);

                if (line.charAt(i) == '>') {
                    break;
                }
            }

            line = tag;

            if (xmlOpeningTag.matcher(line).find()) {
                Matcher matcher = xmlOpeningTag.matcher(line);
                matcher.find();
                return new TagItem(matcher.group(), "opening");
            }

            if (xmlClosingTag.matcher(line).find()) {
                Matcher matcher = xmlClosingTag.matcher(line);
                matcher.find();
                return new TagItem(matcher.group(), "closing");
            }
        }

        return null;
    }

    public static String RemoveXmlCommentsAndVersion(String line) {

        do {
            if (xmlSingleLineComment.matcher(line).find()) {
                Matcher matcher = xmlSingleLineComment.matcher(line);
                matcher.find();

                if (!"".equals(matcher.group())) {
                    line = line.replaceFirst(matcher.group(), "");
                    continue;
                }
            }

            if (xmlVersion.matcher(line).find()) {
                Matcher matcher = xmlVersion.matcher(line);
                matcher.find();
                
                if (!"".equals(matcher.group())) {
                    line = line.replaceFirst(matcher.group(), "");
                    continue;
                }
            }
            
            break;
        } while (true);
        
        return line;
    }
}

package com.fico.pscomponent.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SQLCommandUtil {

    private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");
    private static final Pattern SINGLE_LINE_COMMENT = Pattern.compile("(?m)^--(.*)$");
    private static final Pattern COMMENT = Pattern.compile("(?m)\\/\\*(.*)\\*\\/");
    private static final Pattern COMMA_EOL = Pattern.compile("(?m),\\s+$");
    private static final Pattern EMPTY = Pattern.compile("(?m)\\n");
    private static final Pattern EMPTY_SEMICOL = Pattern.compile("(?m)^;$");
    private static final Pattern FIX1 = Pattern.compile("(?m)\\(\\s+\n");
    private static final Pattern FIX2 = Pattern.compile("(?m)^\\s+\\)\n");


    /**
     * Normalize the string for DB queries. This means
     * normalizing white space.
     */
    String dbNormalize(String s) {
        s = normalizeEmptyAndWhitespace(s);
        if (s == null)
            return null;
        return s.toUpperCase();
    }

    String normalizeEmptyAndWhitespace(String s) {
        s = clean(s);
        if (s == null)
            return null;

        s = WHITE_SPACE.matcher(s).replaceAll(" ");
        return s;
    }

    /**
     * Return a cleaned string or null (if the cleaned string is empty). This
     * method never returns an empty string or a whitespace string. In those
     * cases it will return null. Useful from preventing bad data in the DB,
     * such as empty strings or whitespace.
     */
    String clean(String s) {
        if (s == null)
            return null;
        s = s.trim();
        if (s.length() == 0)
            return null;

        return s;
    }

    public SQLCommandUtil(){
    }

    public List<String> getCommands(String dbCommands){
        String temporary = dbCommands;//dbNormalize(dbCommands);
        temporary = SINGLE_LINE_COMMENT.matcher(temporary).replaceAll("");
        temporary = COMMENT.matcher(temporary).replaceAll("");
        temporary = EMPTY.matcher(temporary).replaceAll("");
        temporary = EMPTY_SEMICOL.matcher(temporary).replaceAll("\n");
        temporary = COMMA_EOL.matcher(temporary).replaceAll(", ");
        temporary = FIX1.matcher(temporary).replaceAll("");
        temporary = FIX2.matcher(temporary).replaceAll("");
        final List<String> lines = Arrays.asList(temporary.split(";"));
        return lines.stream().filter(s -> {
           if(s == null){
               return false;
           }
           if(s.startsWith(";") || s.toLowerCase().startsWith("use") || s.equalsIgnoreCase("")){
               return false;
           }
           return true;
        }).collect(Collectors.toList());
    }

}

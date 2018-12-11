package ru.projects.prog_ja.logic.services.simple.implementations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtil {

    /**
     * Pattern for default string
     * */
    private static final Pattern stringPattern
            = Pattern.compile("^[А-я|Ё|ё|\\s|\\d|\\w|\\-|\\p{Punct}]+$");

    /**
     * Pattern for int value
     * */
    private static final Pattern intPattern
            = Pattern.compile("^[0-9]{1,32}$");

    /**
     * Pattern for long value
     * */
    private static final Pattern longPattern
            = Pattern.compile("^[0-9]{1,64}$");

    /**
     * Pattern for rus name
     * */
    private static final Pattern rusName
            = Pattern.compile("^[А-я]+$");

    private static final Object sKey = new Object();
    private static final Object iKey = new Object();
    private static final Object lKey = new Object();
    private static final Object rnKey = new Object();

    public static Matcher string(String s){
        synchronized (sKey){
            return stringPattern.matcher(s);
        }
    }

    public static Matcher intNumber(String s){
        synchronized (iKey){
            return intPattern.matcher(s);
        }
    }

    public static Matcher longNumber(String s){
        synchronized (lKey){
            return longPattern.matcher(s);
        }
    }

    public static Matcher rusName(String s){
        synchronized (rnKey){
            return rusName.matcher(s);
        }
    }
}

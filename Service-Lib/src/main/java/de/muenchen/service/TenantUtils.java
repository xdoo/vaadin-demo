package de.muenchen.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huningd on 16.12.15.
 */
public class TenantUtils {
    private static final String regex = "(m\\d{2})_.*";
    private final static Pattern pattern = Pattern.compile(regex);

    public static String extractTenantFromUsername(String name) {
        Matcher matcher = pattern.matcher(name);
        if(!matcher.matches()){
            throw new IllegalArgumentException(String.format("User doesn't contains tenant prefix!", name));
        }
        return matcher.group(1);
    }
}

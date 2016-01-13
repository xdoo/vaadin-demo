package de.muenchen.service;

import de.muenchen.service.security.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huningd on 16.12.15.
 */
public class TenantUtils {
    private static final String regexTenant = "(m\\d{2})_.*";
    private static final String regexUsername = "m\\d{2}_(.*)";
    private final static Pattern patternTenant = Pattern.compile(regexTenant);
    private final static Pattern patternUsername = Pattern.compile(regexUsername);

    public static String extractTenantFromUsername(String name) {
        Matcher matcher = patternTenant.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("User doesn't contains tenant prefix!", name));
        }
        return matcher.group(1);
    }

    public static String extractUsernameFromName(String name) {
        Matcher matcher = patternUsername.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Name doesn't contains a valid username!", name));
        }
        return matcher.group(1);
    }

    public static String extractTenantFromPrincipal(Object principal) {
        if (principal instanceof UserInfo) {
            return ((UserInfo) principal).getTenant();
        }
        return extractTenantFromUsername(principal.toString());
    }
}

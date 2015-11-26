package de.muenchen.presentationlib.api;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 * For GAIA-Username and Password.
 */
public class GaiaAccess {
    private final String username;
    private final String password;

    public GaiaAccess(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

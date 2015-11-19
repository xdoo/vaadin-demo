package de.muenchen.presentationlib.api;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
public class RepositoryAccess {
    private final String repoUrl;
    private final String username;
    private final String password;

    public RepositoryAccess(String repoUrl, String username, String password) {
        this.repoUrl = repoUrl;
        this.username = username;
        this.password = password;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

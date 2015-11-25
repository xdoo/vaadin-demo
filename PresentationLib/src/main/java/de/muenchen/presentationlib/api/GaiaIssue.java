package de.muenchen.presentationlib.api;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 * Issue as used by GAIA
 * TODO Maybe has to be changed to be in GAIA API
 */
public class GaiaIssue {
    private String title = "";
    private String body = "";

    public GaiaIssue() {
    }

    public GaiaIssue(String title, String content) {
        this.title = title;
        this.body = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

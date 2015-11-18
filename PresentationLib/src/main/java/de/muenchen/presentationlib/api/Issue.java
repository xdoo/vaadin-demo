package de.muenchen.presentationlib.api;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
public class Issue {
    private String title = "";
    private String content = "";

    public Issue() {
    }

    public Issue(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package de.muenchen.presentationlib.gui;

import de.muenchen.presentationlib.api.GaiaIssue;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
public interface IssueService {
    void createIssue(GaiaIssue issue);
    void login(String username, String Password);
}

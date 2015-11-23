package de.muenchen.presentationlib.api;

/**
 *
 * @author maximilian.zollbrecht
 */
public interface GaiaIssueRestClient {

    void createIssue(String applicationName, GaiaIssue issue);
    
}

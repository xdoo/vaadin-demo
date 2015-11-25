package de.muenchen.presentationlib.api;

import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by maximilian.zollbrecht on 23.11.15.
 */
public class GaiaIssueRestClientImpl implements GaiaIssueRestClient {

    private RestTemplate template;
    private URI baseUri;

    /**
     * Relative url to create issues. ProjectName needs to be appended as url-parameter
     */
    private static final String ISSUE_URL = "/businessActions/issueErstellen?project=";

    public GaiaIssueRestClientImpl(RestTemplate template, URI baseUri) {
        if (template == null) throw new NullPointerException("RestTemplate must not be null!");
        if (baseUri == null) throw new NullPointerException("BaseUri must not be null!");
        this.template = template;
        this.baseUri = baseUri;
    }

    @Override
    public void createIssue(String applicationName, GaiaIssue issue){
        String url = baseUri+ISSUE_URL+applicationName;
        template.postForEntity(url, issue, Void.class);
    }
}

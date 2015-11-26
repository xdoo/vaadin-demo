package de.muenchen.presentationlib.gui;

import com.netflix.discovery.DiscoveryClient;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.presentationlib.api.GaiaIssue;
import de.muenchen.presentationlib.api.GaiaAccess;
import de.muenchen.presentationlib.api.GaiaIssueRestClient;
import de.muenchen.presentationlib.api.GaiaIssueRestClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.net.URI;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
@SpringComponent("GAIAIssueService")
@UIScope
public class GaiaIssueService implements IssueService {

    private final Logger LOG = LoggerFactory.getLogger(GaiaIssueRestClientImpl.class);

    public static final String GRANT_TYPE = "password";

    public static final String HOSTNAME = "Gaia";

    private final String GAIA_BASE_URL;

    private static final String TOKEN_URL = "/oauth/token";

    private OAuth2RestTemplate template;

    @Value("${security.oauth2.client.id}")
    private String clientID = "acme";

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    DiscoveryClient discoveryClient;

    private GaiaIssueRestClient restClient;

    public GaiaIssueService() {
        if(discoveryClient!=null) {
            GAIA_BASE_URL = discoveryClient.getNextServerFromEureka(HOSTNAME, false).getHomePageUrl();
            LOG.info("Loaded Gaia url: "+GAIA_BASE_URL);
        } else {
            LOG.warn("No discovery client found. Using localhost.");
            GAIA_BASE_URL = "http://localhost:8080";
        }
    }

    @Override
    public void createIssue(GaiaIssue issue){
        restClient.createIssue(applicationName, issue);
    }

    @Override
    public void login(String username, String password){
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setUsername(username);
        resource.setPassword(password);
        resource.setGrantType(GRANT_TYPE);
        resource.setClientId(clientID);
        resource.setAccessTokenUri(GAIA_BASE_URL+TOKEN_URL);

        template = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext());

        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        this.restClient = new GaiaIssueRestClientImpl(template, URI.create(GAIA_BASE_URL));
    }
}

package de.muenchen.presentationlib.gui;

import de.muenchen.presentationlib.api.Issue;
import de.muenchen.presentationlib.api.RepositoryAccess;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
public class GitIssueService implements IssueService {

    private final Logger LOG = LoggerFactory.getLogger(GitIssueService.class);

    private final RepositoryAccess repositoryAccess;

    private final RestTemplate template;

    public GitIssueService(RepositoryAccess repositoryAccess) {
        this.repositoryAccess = repositoryAccess;

        template = new RestTemplate();

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        InetSocketAddress address = new InetSocketAddress("10.158.0.79",80);
        Proxy proxy = new Proxy(Proxy.Type.HTTP,address);
        factory.setProxy(proxy);
        template.setRequestFactory(factory);
    }

    @Override
    public void createIssue(Issue issue){
        String base64Creds = new String(Base64.encodeBase64((repositoryAccess.getUsername()+":"+repositoryAccess.getPassword()).getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<Issue> request = new HttpEntity<>(issue, headers);

        template.postForEntity(repositoryAccess.getRepoUrl(), request, Void.class);
    }
}

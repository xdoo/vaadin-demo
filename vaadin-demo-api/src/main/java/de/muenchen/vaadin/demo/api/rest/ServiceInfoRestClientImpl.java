package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.ServiceInfo;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus.straube
 */
@Service
public class ServiceInfoRestClientImpl implements ServiceInfoRestClient {

    RestTemplate restTemplate = new RestTemplate();

    @Value(value = "${service.info.url}")
    private String url;

    @Override
    public ServiceInfo getServiceInfo() {
        return readSingleSource();
    }

    public ServiceInfo readSingleSource() {
        /**
         * Erster Test für Durchstich TODO Umstellung auf die Logindaten aus der
         * GUI
         */

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans", "test"));

        HttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(requestFactory);

        ResponseEntity<ServiceInfoResource> resource = this.restTemplate.getForEntity(this.url, ServiceInfoResource.class);
        return ServiceInfoAssembler.fromResource(resource.getBody());

    }

}

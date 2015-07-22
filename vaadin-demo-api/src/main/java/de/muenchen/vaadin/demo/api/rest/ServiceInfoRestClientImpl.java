package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.ServiceInfo;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
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
         * Erster Test für Durchstich
         * TODO Umstellung auf die Logindaten aus der GUI
         */
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
            SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans", "test"));
            
            HttpClient httpClient = HttpClientBuilder.create()
                    .setSSLSocketFactory(connectionFactory)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .build();
            
            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            restTemplate = new RestTemplate(requestFactory);
            ResponseEntity<ServiceInfoResource> resource = this.restTemplate.getForEntity(this.url, ServiceInfoResource.class);
            return ServiceInfoAssembler.fromResource(resource.getBody());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServiceInfoRestClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(ServiceInfoRestClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ServiceInfoRestClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}

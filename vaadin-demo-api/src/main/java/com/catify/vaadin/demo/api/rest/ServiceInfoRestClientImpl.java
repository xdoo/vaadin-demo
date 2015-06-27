package com.catify.vaadin.demo.api.rest;

import com.catify.vaadin.demo.api.domain.ServiceInfo;
import com.catify.vaadin.demo.api.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
        ResponseEntity<ServiceInfoResource> resource = this.restTemplate.getForEntity(this.url, ServiceInfoResource.class);
        return ServiceInfoAssembler.fromResource(resource.getBody());
    }
    
}

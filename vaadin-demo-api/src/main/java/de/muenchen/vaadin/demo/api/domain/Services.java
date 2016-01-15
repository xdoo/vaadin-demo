package de.muenchen.vaadin.demo.api.domain;

import de.muenchen.vaadin.demo.apilib.domain.DomainService;

/**
 * Enum for all available Services
 *
 * Created by rene.zarwel on 30.11.15.
 */
public enum  Services implements DomainService{
    SERVICE("vaadin-microservice");

    String id;

    Services(String id) {
        this.id = id;
    }

    @Override
    public String getClientId(){
        return id;
    }
}

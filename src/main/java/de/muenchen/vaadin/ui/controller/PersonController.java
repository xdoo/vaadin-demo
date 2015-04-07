/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.controller;

import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author claus.straube
 */
@Component
public class PersonController {
    
    @Autowired
    PersonService service;
    
    public Person createPerson() {
        return new Person();
    }
    
    public void savePerson(Person person) {
        service.updatePerson(person);
    }
    
}

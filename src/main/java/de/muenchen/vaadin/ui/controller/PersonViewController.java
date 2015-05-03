/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.controller;

import de.muenchen.vaadin.domain.Person;
import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.app.views.events.CreatePersonEvent;
import de.muenchen.vaadin.ui.app.views.events.PersonEvent;
import de.muenchen.vaadin.ui.app.views.events.SelectPersonEvent;
import de.muenchen.vaadin.ui.app.views.events.UpdatePersonEvent;
import de.muenchen.vaadin.ui.components.CreatePersonForm;
import de.muenchen.vaadin.ui.components.PersonTable;
import de.muenchen.vaadin.ui.components.UpdatePersonForm;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import javax.annotation.PostConstruct;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

/**
 *
 * @author claus.straube
 */
public class PersonViewController implements EventBusListener<PersonEvent> {
    
    PersonService service;
    VaadinUtil util;
    EventBus eventbus;

    public PersonViewController(PersonService service, VaadinUtil util, EventBus eventbus) {
        this.service = service;
        this.util = util;
        this.eventbus = eventbus;
        
        this.eventbus.subscribe(this, true);
    }
     
    // components
    CreatePersonForm createPersonForm;
    UpdatePersonForm updatePersonForm;
    PersonTable personTable;
   

    public EventBus getEventbus() {
        return eventbus;
    }

    public VaadinUtil getUtil() {
        return util;
    }
    
    public Person createPerson() {
        return new Person();
    }
    
    public void savePerson(Person person) {
        service.updatePerson(person);
    }

    public CreatePersonForm getCreatePersonForm() {
        if(this.createPersonForm == null) {
            this.createPersonForm = new CreatePersonForm(util, service, eventbus);
        }
        return createPersonForm;
    }

    public UpdatePersonForm getUpdatePersonForm() {
        if(this.updatePersonForm == null) {
            this.updatePersonForm = new UpdatePersonForm(util, service, eventbus);
        }
        return updatePersonForm;
    }

    public PersonTable getPersonTable() {
        if(this.personTable == null) {
            this.personTable = new PersonTable(util, service, eventbus);
        }
        return personTable;
    }
    
    
    
    @Override
    public void onEvent(org.vaadin.spring.events.Event<PersonEvent> e) {
        PersonEvent event = e.getPayload();
        
        // create
        if(event instanceof CreatePersonEvent) {
            this.personTable.add(event.getPerson());
        } 
        
        // update
        if(event instanceof UpdatePersonEvent) {
            this.personTable.update(event.getPerson());
        }
        
        // select
        if(event instanceof SelectPersonEvent) {
            this.updatePersonForm.select(event.getItem());
        }
    }
    
}

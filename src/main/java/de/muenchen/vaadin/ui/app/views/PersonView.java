/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.app.views.events.CreatePersonEvent;
import de.muenchen.vaadin.ui.app.views.events.PersonEvent;
import de.muenchen.vaadin.ui.app.views.events.SelectPersonEvent;
import de.muenchen.vaadin.ui.app.views.events.UpdatePersonEvent;
import de.muenchen.vaadin.ui.components.CreatePersonForm;
import de.muenchen.vaadin.ui.components.PersonTable;
import de.muenchen.vaadin.ui.components.UpdatePersonForm;
import de.muenchen.vaadin.ui.controller.PersonController;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.navigator.annotation.VaadinView;

/**
 *
 * @author claus.straube
 */
@VaadinView(name = PersonView.NAME)
@VaadinUIScope
public class PersonView extends VerticalLayout implements View, EventBusListener<PersonEvent>{

    public static final String NAME = "person";
    
    @Autowired
    PersonController controller;
    
    @Autowired
    VaadinUtil util;
    
    @Autowired
    PersonService service;
    
    @Autowired
    EventBus eventbus;
    
    // components
    CreatePersonForm createPersonForm;
    UpdatePersonForm updatePersonForm;
    PersonTable personTable;
    
    
    @PostConstruct
    private void postConstruct() {
        this.configure();
        this.createPersonForm = new CreatePersonForm(util, service, eventbus);
        this.updatePersonForm = new UpdatePersonForm(util, service, eventbus);
        this.personTable = new PersonTable(util, service, eventbus);
        
        // headline
        Label headline = new Label("Person View");
        headline.addStyleName(ValoTheme.LABEL_H3);
        headline.addStyleName(ValoTheme.LABEL_COLORED);
        addComponent(headline);
        
        // body
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(this.createPersonForm);
        horizontalLayout.addComponent(this.updatePersonForm);
        horizontalLayout.setMargin(true);
        addComponent(horizontalLayout);
        
        addComponent(this.personTable);
        
        this.eventbus.subscribe(this, true);
    }
    
    private void configure() {
        setSizeFull();
        this.setHeightUndefined();
//        setSpacing(true);
        setMargin(true);
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // not implemented
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

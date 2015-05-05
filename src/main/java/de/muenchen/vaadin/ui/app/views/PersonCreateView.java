/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views;

import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.annotation.VaadinView;

/**
 *
 * @author claus
 */
@VaadinView(name = PersonCreateView.NAME)
@VaadinUIScope
public class PersonCreateView extends DefaultPersonView {
    
    public static final String NAME = "person_create_view";

    @Autowired
    public PersonCreateView(PersonService service, VaadinUtil util, EventBus eventbus, MainUI ui) {
        super(service, util, eventbus, ui);
    }

    @Override
    protected void site() {
        addComponent(this.controller.generateCreatePersonForm(PersonTableView.NAME));
    }
    
}

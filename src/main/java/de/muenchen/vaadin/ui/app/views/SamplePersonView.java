/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views;

import de.muenchen.vaadin.services.PersonService;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.annotation.VaadinView;

/**
 *
 * @author claus
 */
@VaadinView(name = SamplePersonView.NAME)
@VaadinUIScope
public class SamplePersonView extends DefaultPersonView {
    
    public static final String NAME = "sample_person_view";

    @Autowired
    public SamplePersonView(PersonService service, VaadinUtil util, EventBus eventbus) {
        super(service, util, eventbus);
    }

    @Override
    protected void site() {
        addComponent(this.controller.getCreatePersonForm());
        addComponent(this.controller.getUpdatePersonForm());
        addComponent(this.controller.getPersonTable());
    }
    
}

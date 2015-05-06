/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views;

import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.CreatePersonButton;
import de.muenchen.vaadin.ui.components.PersonTable;
import de.muenchen.vaadin.ui.controller.PersonViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.annotation.VaadinView;

/**
 *
 * @author claus
 */
@VaadinView(name = PersonTableView.NAME)
@VaadinUIScope
public class PersonTableView extends DefaultPersonView {
    
    public static final String NAME = "person_table_view";
    
    @Autowired
    public PersonTableView(PersonViewController controller, EventBus eventbus, MainUI ui) {
        super(controller, eventbus, ui);
    }

    @Override
    protected void site() {
        CreatePersonButton button = new CreatePersonButton(controller, PersonCreateView.NAME);
        PersonTable table = this.controller.generatePersonTable(PersonUpdateView.NAME);
        
        addComponent(new VerticalLayout(button, table));
    }
    
}

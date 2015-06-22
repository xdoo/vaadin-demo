/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views;

import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.CreateBuergerButton;
import de.muenchen.vaadin.ui.components.BuergerTable;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
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
    public PersonTableView(BuergerViewController controller, EventBus eventbus, MainUI ui) {
        super(controller, eventbus, ui);
    }

    @Override
    protected void site() {
        CreateBuergerButton button = new CreateBuergerButton(controller, PersonCreateView.NAME);
        BuergerTable table = this.controller.generatePersonTable(PersonUpdateView.NAME);
        
        VerticalLayout layout = new VerticalLayout(button, table);
        layout.setSpacing(true);
        
        addComponent(layout);
    }
    
}

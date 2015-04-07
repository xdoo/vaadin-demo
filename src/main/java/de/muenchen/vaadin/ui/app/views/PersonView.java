/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.components.PersonForm;
import de.muenchen.vaadin.ui.controller.PersonController;
import de.muenchen.vaadin.ui.util.VaadinUtil;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;

/**
 *
 * @author claus.straube
 */
@VaadinView(name = PersonView.NAME)
@VaadinUIScope
public class PersonView extends VerticalLayout implements View {

    public static final String NAME = "person";
    
    @Autowired
    PersonController controller;
    
    @PostConstruct
    private void postConstruct() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        addComponent(new Label("<h3>Person View</h3>", ContentMode.HTML));
        addComponent(new PersonForm(controller.createPerson()));
        addComponent(VaadinUtil.createNavigationButton("Go To Main View", MainView.NAME));
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
    
}

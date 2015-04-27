/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views;

import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.navigator.annotation.VaadinView;

/**
 *
 * @author claus
 */
@VaadinView(name = SamplePersonView.NAME)
@VaadinUIScope
public class SamplePersonView extends DefaultPersonView {
    
    public static final String NAME = "sample_person_view";

    @Override
    protected void site() {
        addComponent(this.createPersonForm);
        addComponent(this.updatePersonForm);
        addComponent(this.personTable);
    }
    
}

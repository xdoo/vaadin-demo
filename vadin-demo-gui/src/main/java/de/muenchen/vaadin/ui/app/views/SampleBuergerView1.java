/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.app.views;

import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.annotation.VaadinView;

/**
 *
 * @author claus
 */
@VaadinView(name = SampleBuergerView1.NAME)
@VaadinUIScope
public class SampleBuergerView1 extends DefaultBuergerView {
    
    public static final String NAME = "sample_buerger_view1";
    
    @Autowired
    public SampleBuergerView1(BuergerViewController controller, EventBus eventbus, MainUI ui) {
        super(controller, eventbus, ui);
    }

    @Override
    protected void site() {
        addComponent(this.controller.generatePersonTable(BuergerUpdateView.NAME));
    }
    
}

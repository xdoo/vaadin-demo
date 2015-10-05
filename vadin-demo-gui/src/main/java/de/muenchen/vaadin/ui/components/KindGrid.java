/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 *
 * @author maximilian.schug
 */
public class KindGrid extends GenericGrid {

    public KindGrid(BuergerI18nResolver resolver) {
        super(resolver, Buerger.class);
    }

    @Override
    public void accept(reactor.bus.Event<BuergerDatastore> eventWrapper) {
        //super.accept(eventWrapper);
        final BuergerDatastore event = eventWrapper.getData();
        if (this.getContainerDataSource().size() == 0)
            this.setContainerDataSource(event.getSelectedBuergerKinder());
    }
}

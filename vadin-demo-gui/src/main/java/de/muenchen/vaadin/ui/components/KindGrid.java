/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.services.model.BuergerReadOnlyModel;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 *
 * @author maximilian.schug
 */
public class KindGrid extends GenericGrid {

    public KindGrid(BuergerViewController controller) {
        super(controller, Buerger.class);
    }

    @Override
    public void accept(reactor.bus.Event<BuergerReadOnlyModel> eventWrapper) {
        //super.accept(eventWrapper);
        final BuergerReadOnlyModel event = eventWrapper.getData();
        addAll(event.getSelectedBuergerKinder());
    }
}

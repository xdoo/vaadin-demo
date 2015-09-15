package de.muenchen.vaadin.ui.components;

import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 * Created by rene.zarwel on 01.09.15.
 */
public class BuergerTable extends GenericTable<LocalBuerger> {

    public BuergerTable(BuergerViewController controller, TableActionButton.Builder... buttonBuilders) {
        super(controller, LocalBuerger.class, buttonBuilders);
    }

}

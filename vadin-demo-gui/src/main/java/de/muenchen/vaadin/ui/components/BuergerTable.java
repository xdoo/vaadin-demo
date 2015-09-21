package de.muenchen.vaadin.ui.components;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.TableActionButton;
import de.muenchen.vaadin.guilib.components.GenericTable;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 * Created by rene.zarwel on 01.09.15.
 */
public class BuergerTable extends GenericTable<Buerger> {

    public BuergerTable(BuergerViewController controller, TableActionButton.Builder... buttonBuilders) {
        super(controller, Buerger.class, buttonBuilders);
    }

}

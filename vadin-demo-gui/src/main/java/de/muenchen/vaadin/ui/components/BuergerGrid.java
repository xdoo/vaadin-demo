package de.muenchen.vaadin.ui.components;

import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.ui.app.views.BuergerCreateView;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.app.views.BuergerUpdateView;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author claus.straube, arne.schoentag
 */
public class BuergerGrid extends GenericGrid {

    protected static final Logger LOG = LoggerFactory.getLogger(BuergerGrid.class);


    public BuergerGrid(final BuergerViewController controller) {

        super(controller.getModel().getBuergers(),
                Buerger.Field.getProperties());

        this
                .activateDoubleClickToRead(BuergerDetailView.NAME)
                .activateCreate(BuergerCreateView.NAME)
                .activateSearch()
                .activateEdit(BuergerUpdateView.NAME)
                .activateRead(BuergerDetailView.NAME)
                .activateCopy()
                .activateDelete();

    }

}

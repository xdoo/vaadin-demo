package de.muenchen.vaadin.guilib.security.components;

import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.guilib.security.controller.Authority_ViewController;
import de.muenchen.vaadin.guilib.components.GenericGrid;

/**
 * @author rene.zarwel
 */
public class Authority_Grid extends GenericGrid<Authority_> {

    public Authority_Grid(final Authority_ViewController controller) {
        super(controller.getModel().getAuthoritys(),
                Authority_.Field.getProperties());
    }

}

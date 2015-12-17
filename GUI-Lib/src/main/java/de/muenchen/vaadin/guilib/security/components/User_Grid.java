package de.muenchen.vaadin.guilib.security.components;

import de.muenchen.vaadin.demo.apilib.local.User_;
import de.muenchen.vaadin.guilib.security.controller.User_ViewController;
import de.muenchen.vaadin.guilib.components.GenericGrid;

/**
 * @author rene.zarwel
 */
public class User_Grid extends GenericGrid<User_> {

    public User_Grid(final User_ViewController controller) {
        super(controller.getModel().getUsers(),
                User_.Field.getProperties());
    }

}

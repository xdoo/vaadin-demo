package de.muenchen.vaadin.guilib.security.components;

import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.guilib.security.controller.User_ViewController;
import de.muenchen.vaadin.guilib.components.GenericGrid;

/**
 * @author rene.zarwel
 */
public class User_Authoritys_ReadGrid extends GenericGrid<Authority_> {

    public User_Authoritys_ReadGrid(final User_ViewController controller, final String navigateToRead) {
        super(controller.getModel().getSelectedUserAuthorities(),
                Authority_.Field.getProperties());
        this.activateSearch().activateRead(navigateToRead).activateDoubleClickToRead(navigateToRead);
    }

}

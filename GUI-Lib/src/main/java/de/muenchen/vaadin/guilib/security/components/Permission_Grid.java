package de.muenchen.vaadin.guilib.security.components;

import de.muenchen.vaadin.demo.apilib.local.Permission;
import de.muenchen.vaadin.guilib.security.controller.Permission_ViewController;
import de.muenchen.vaadin.guilib.components.GenericGrid;

/**
 * @author rene.zarwel
 */
public class Permission_Grid extends GenericGrid<Permission> {

    public Permission_Grid(final Permission_ViewController controller) {
        super(controller.getModel().getPermissions(),
                Permission.Field.getProperties());
    }

}

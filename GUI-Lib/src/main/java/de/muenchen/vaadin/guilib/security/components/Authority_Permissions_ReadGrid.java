package de.muenchen.vaadin.guilib.security.components;

import de.muenchen.vaadin.demo.apilib.local.Permission;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.Authority_SingleActions;
import de.muenchen.vaadin.guilib.security.controller.Authority_ViewController;
import de.muenchen.vaadin.guilib.components.GenericGrid;

/**
 * @author rene.zarwel
 */
public class Authority_Permissions_ReadGrid extends GenericGrid<Permission> {

	private final Authority_ViewController controller;

    public Authority_Permissions_ReadGrid(final Authority_ViewController controller, final String navigateToRead) {
    	super(controller.getModel().getSelectedAuthorityPermissions(),
                Permission.Field.getProperties());
        this.controller = controller;
        this.activateSearch().activateRead(navigateToRead).activateDoubleClickToRead(navigateToRead);
    }
    
    /**
	 * Aktualisiert die Relationen in dieser Grid vom Server. Sollte in der init-Methode der View aufgerufen werden um einen
	 * Konsistenten Datenstand zu gewährleisten.
	 */
	public void reload(){
	    final Authority_SingleActions singleActions = new Authority_SingleActions(controller.getModel().getSelectedAuthority()::get);
	    singleActions.reRead(null);
	}

}

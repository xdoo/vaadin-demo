package de.muenchen.vaadin.guilib.security.components;

import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.User_SingleActions;
import de.muenchen.vaadin.guilib.security.controller.User_ViewController;
import de.muenchen.vaadin.guilib.components.GenericGrid;

/**
 * @author rene.zarwel
 */
public class User_Authorities_ReadGrid extends GenericGrid<Authority_> {

	private final User_ViewController controller;

    public User_Authorities_ReadGrid(final User_ViewController controller, final String navigateToRead) {
    	super(controller.getModel().getSelectedUserAuthorities(),
                Authority_.Field.getProperties());
        this.controller = controller;
        this.activateSearch().activateRead(navigateToRead).activateDoubleClickToRead(navigateToRead);
    }
    
    /**
	 * Aktualisiert die Relationen in dieser Grid vom Server. Sollte in der init-Methode der View aufgerufen werden um einen
	 * Konsistenten Datenstand zu gewährleisten.
	 */
	public void reload(){
	    final User_SingleActions singleActions = new User_SingleActions(controller.getModel().getSelectedUser()::get);
	    singleActions.reRead(null);
	}

}

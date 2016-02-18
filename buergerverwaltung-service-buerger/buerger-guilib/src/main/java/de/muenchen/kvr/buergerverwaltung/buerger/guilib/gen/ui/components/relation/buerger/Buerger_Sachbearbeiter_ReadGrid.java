package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.buerger;

import com.vaadin.ui.Component;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Buerger_ViewController;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Sachbearbeiter_ViewController;

/**
 * @author rene.zarwel
 */
public class Buerger_Sachbearbeiter_ReadGrid extends GenericGrid<Sachbearbeiter_> {

	private final Buerger_ViewController controller;
	
	/**
	 * @param controller
	 * @param navigateToRead detail button will not be shown if null
	 */
    public Buerger_Sachbearbeiter_ReadGrid(final Buerger_ViewController controller, final String navigateToRead) {
    	super(controller.getModel().getSelectedBuergerSachbearbeiter(),
                Sachbearbeiter_.Field.getProperties());
        this.controller = controller;
        this.activateSearch();
        if(navigateToRead != null)
        	this.activateRead(navigateToRead).activateDoubleClickToRead(navigateToRead);
    }
    
    /**
	 * Aktualisiert die Relationen in dieser Grid vom Server. Sollte in der init-Methode der View aufgerufen werden um einen
	 * Konsistenten Datenstand zu gewährleisten.
	 */
	public void reload(){
	    final Buerger_SingleActions singleActions = new Buerger_SingleActions(controller.getModel().getSelectedBuerger()::get);
	    singleActions.reRead(null);
	}

	public Component addButton(ActionButton button){
    	addComponent(button);
    	return this;
    }
}

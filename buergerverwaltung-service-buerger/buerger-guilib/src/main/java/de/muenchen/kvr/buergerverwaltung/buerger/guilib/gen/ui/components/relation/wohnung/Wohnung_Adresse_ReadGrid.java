package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.wohnung;

import com.vaadin.ui.Component;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Adresse_;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.wohnung.Wohnung_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Wohnung_ViewController;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Adresse_ViewController;

/**
 * @author rene.zarwel
 */
public class Wohnung_Adresse_ReadGrid extends GenericGrid<Adresse_> {

	private final Wohnung_ViewController controller;
	
	/**
	 * @param controller
	 * @param navigateToRead detail button will not be shown if null
	 */
    public Wohnung_Adresse_ReadGrid(final Wohnung_ViewController controller, final String navigateToRead) {
    	super(controller.getModel().getSelectedWohnungAdresse(),
                Adresse_.Field.getProperties());
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
	    final Wohnung_SingleActions singleActions = new Wohnung_SingleActions(controller.getModel().getSelectedWohnung()::get);
	    singleActions.reRead(null);
	}

	public Component addButton(ActionButton button){
    	addComponent(button);
    	return this;
    }
}

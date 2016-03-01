package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.wohnung;

import com.vaadin.ui.Component;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Wohnung_;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Wohnung_ViewController;

/**
 * @author rene.zarwel
 */
public class Wohnung_Grid extends GenericGrid<Wohnung_> {

    public Wohnung_Grid(final Wohnung_ViewController controller) {
        super(controller.getModel().getWohnungs(),
                Wohnung_.Field.getProperties());
    }
    
    public Component addButton(ActionButton button){
    	addComponent(button);
    	return this;
    }

}

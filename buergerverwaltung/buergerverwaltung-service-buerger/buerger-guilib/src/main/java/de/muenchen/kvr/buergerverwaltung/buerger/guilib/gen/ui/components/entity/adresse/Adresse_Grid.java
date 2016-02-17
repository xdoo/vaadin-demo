package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.adresse;

import com.vaadin.ui.Component;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Adresse_;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Adresse_ViewController;

/**
 * @author rene.zarwel
 */
public class Adresse_Grid extends GenericGrid<Adresse_> {

    public Adresse_Grid(final Adresse_ViewController controller) {
        super(controller.getModel().getAdresses(),
                Adresse_.Field.getProperties());
    }
    
    public Component addButton(ActionButton button){
    	addComponent(button);
    	return this;
    }

}

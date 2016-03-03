package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.sachbearbeiter;

import com.vaadin.ui.Component;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Sachbearbeiter_ViewController;

/**
 * @author rene.zarwel
 */
public class Sachbearbeiter_Grid extends GenericGrid<Sachbearbeiter_> {

    public Sachbearbeiter_Grid(final Sachbearbeiter_ViewController controller) {
        super(controller.getModel().getSachbearbeiters(),
                Sachbearbeiter_.Field.getProperties());
    }
    
    public Component addButton(ActionButton button){
    	addComponent(button);
    	return this;
    }

}

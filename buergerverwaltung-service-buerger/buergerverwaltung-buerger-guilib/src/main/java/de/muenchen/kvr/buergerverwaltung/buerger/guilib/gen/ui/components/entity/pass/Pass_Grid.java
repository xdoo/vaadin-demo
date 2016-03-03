package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.pass;

import com.vaadin.ui.Component;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Pass_;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Pass_ViewController;

/**
 * @author rene.zarwel
 */
public class Pass_Grid extends GenericGrid<Pass_> {

    public Pass_Grid(final Pass_ViewController controller) {
        super(controller.getModel().getPasss(),
                Pass_.Field.getProperties());
    }
    
    public Component addButton(ActionButton button){
    	addComponent(button);
    	return this;
    }

}

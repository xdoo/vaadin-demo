package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.pass;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;

import com.vaadin.data.util.BeanItemContainer;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Pass_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.pass.Pass_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.services.model.Pass_Datastore;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
/**
 * Provides a simple {@link Pass_Form} that always shows the {@link Pass_Datastore#getSelectedPass()}.
 *
 * @author p.mueller
 * @version 1.0
 */
public class Pass_SelectedForm extends Pass_Form {

    /**
     * Creates a new Pass_Form that updates its Pass_ to the {@link Pass_Datastore#getSelectedPass()}
     * from the Eventbus.
     */
    public Pass_SelectedForm() {
        getEventBus().on(new ResponseEntityKey(Pass_Form.ENTITY_CLASS).toSelector(), this::update);
    }
    
    public void reLoad() {
        final Pass_SingleActions singleActions = new Pass_SingleActions(this::getPass);
        singleActions.reRead(null);
    }

    /**
     * Update the Pass_ of this Form to the selected one form the DataStore.
     *
     * @param event
     */
    private void update(reactor.bus.Event<?> event) {
        final Pass_Datastore data = (Pass_Datastore) event.getData();
        if(data.getSelectedPass().isPresent()){
        	Pass_ pass = data.getSelectedPass().get();
			setPass(pass);
		}
    }
}

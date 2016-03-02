package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.wohnung;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;

import com.vaadin.data.util.BeanItemContainer;
import de.muenchen.eventbus.selector.entity.ResponseEntityKey;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Wohnung_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.wohnung.Wohnung_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.services.model.Wohnung_Datastore;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
/**
 * Provides a simple {@link Wohnung_Form} that always shows the {@link Wohnung_Datastore#getSelectedWohnung()}.
 *
 * @author p.mueller
 * @version 1.0
 */
public class Wohnung_SelectedForm extends Wohnung_Form {

    /**
     * Creates a new Wohnung_Form that updates its Wohnung_ to the {@link Wohnung_Datastore#getSelectedWohnung()}
     * from the Eventbus.
     */
    public Wohnung_SelectedForm() {
        getEventBus().on(new ResponseEntityKey(Wohnung_Form.ENTITY_CLASS).toSelector(), this::update);
    }
    
    public void reLoad() {
        final Wohnung_SingleActions singleActions = new Wohnung_SingleActions(this::getWohnung);
        singleActions.reRead(null);
    }

    /**
     * Update the Wohnung_ of this Form to the selected one form the DataStore.
     *
     * @param event
     */
    private void update(reactor.bus.Event<?> event) {
        final Wohnung_Datastore data = (Wohnung_Datastore) event.getData();
        if(data.getSelectedWohnung().isPresent()){
        	Wohnung_ wohnung = data.getSelectedWohnung().get();
			setWohnung(wohnung);
		}
    }
}

package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.buerger;

import de.muenchen.eventbus.selector.entity.ResponseEntityKey;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.services.model.Buerger_Datastore;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.wohnung.Wohnung_Form;

/**
 * Created by p.mueller on 13.10.15.
 */
public class Buerger_Hauptwohnung_Form extends Wohnung_Form {
    
    /**
     * Create a new Wohnung_Form using the specified i18nResolver and the eventbus.
     * <p/>
     * This Form is only the plain fields for input, and has no additional components or buttons. You can use {@link
     * Wohnung_Form#setReadOnly(boolean)} for a readonly mode.
     */
    public Buerger_Hauptwohnung_Form() {
        getEventBus().on(new ResponseEntityKey(Buerger_.class).toSelector(), this::update);
    }

    public void reLoad() {
        final Buerger_SingleActions singleActions = new Buerger_SingleActions(() -> null);
        singleActions.reRead(null);
    }

    private void update(reactor.bus.Event<?> event) {
        final Buerger_Datastore data = (Buerger_Datastore) event.getData();
        setWohnung(data.getSelectedBuergerHauptwohnung().orElse(null));
    }
}


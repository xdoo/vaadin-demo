package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 * Created by arne.schoentag on 02.09.15.
 */
public class HistoryTable extends GenericTable<Buerger> {

    //protected static final Logger LOG = LoggerFactory.getLogger(BuergerReadForm.class);

    public HistoryTable(BuergerViewController controller, final TableActionButton.Builder... buttonfactory) {
        super(controller, Buerger.class, buttonfactory);
    }

    /**
     * Eventhandler für Eventbus
     *
     * @param event the event
     */
    @Subscribe
    public void update(BuergerComponentEvent event) {

        if (event.getEventType().equals(EventType.HISTORY)) {
            LOG.error(event.getEntities().toString());
            this.addAll(event.getEntities());
        }

    }
}

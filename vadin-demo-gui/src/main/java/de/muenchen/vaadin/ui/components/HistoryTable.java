package de.muenchen.vaadin.ui.components;

import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * Created by arne.schoentag on 02.09.15.
 */
public class HistoryTable extends GenericTable<BuergerResource> implements Consumer<Event<ComponentEvent<BuergerResource>>> {

    //protected static final Logger LOG = LoggerFactory.getLogger(BuergerReadForm.class);

    public HistoryTable(BuergerViewController controller, final TableActionButton.Builder... buttonfactory) {
        super(controller, BuergerResource.class, buttonfactory);
    }

    /**
     * Eventhandler für Eventbus
     *
     * @param eventWrapper the event
     */
    @Override
    public void accept(reactor.bus.Event<ComponentEvent<BuergerResource>> eventWrapper) {
        ComponentEvent event = eventWrapper.getData();

        if (event.getEventType().equals(EventType.HISTORY)) {
            LOG.debug(event.getEntities().toString());
            this.addAll(event.getEntities());
        }

    }
}

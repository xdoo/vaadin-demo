package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.Grid;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * Created by arne.schoentag on 18.09.15.
 */
public class GenericGrid extends Grid implements Consumer<Event<BuergerDatastore>> {

    /**
     * The constant LOG.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(GenericGrid.class);

    /**
     * Instantiates a new Generic table.
     *
     * @param controller  the controller
     * @param entityClass the entity class
     */
    public GenericGrid(final I18nResolver controller, Class<Buerger> entityClass) {
    }

    /**
     * Accept method to register to receive AppEvents.
     *
     * @param eventWrapper
     */
    @Override
    public void accept(reactor.bus.Event<BuergerDatastore> eventWrapper) {
        BuergerDatastore event = eventWrapper.getData();
        if (this.getContainerDataSource().size() == 0) //TODO HACK
            this.setContainerDataSource(event.getBuergers());
    }
}

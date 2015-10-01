package de.muenchen.vaadin.services.model;

import com.vaadin.data.util.BeanItemContainer;
import de.muenchen.vaadin.demo.api.local.Buerger;

import java.util.Optional;

/**
 * Provides a simple Model for the Buergers in the GUI.
 *
 * @author fabian.holtkoetter p.mueller
 * @version 1.0
 */
public class BuergerModel implements BuergerReadOnlyModel {
    /** All the kinders of the selectedBuerger. */
    private final BeanItemContainer<Buerger> selectedBuergerKinder = new BeanItemContainer<>(Buerger.class);
    /** All the partners of the selectedBuerger. */
    private final BeanItemContainer<Buerger> selectedBuergerPartner = new BeanItemContainer<>(Buerger.class);
    /** A List of all the Buergers, possible reduced by the query. */
    private final BeanItemContainer<Buerger> buergers = new BeanItemContainer<>(Buerger.class);
    /** The current (single or none) selected Buerger in the GUI. */
    private Optional<Buerger> selectedBuerger = Optional.empty();
    /** The query to filter the buergers. */
    private Optional<String> query = Optional.empty();

    @Override
    public Optional<Buerger> getSelectedBuerger() {
        return selectedBuerger;
    }

    /**
     * Set the Selected Buerger, if null the Optional will be empty.
     *
     * @param selectedBuerger The Buerger to set as the selected one.
     */
    public void setSelectedBuerger(Buerger selectedBuerger) {
        this.selectedBuerger = Optional.ofNullable(selectedBuerger);
    }

    @Override
    public BeanItemContainer<Buerger> getBuergers() {
        return buergers;
    }

    @Override
    public Optional<String> getQuery() {
        return query;
    }

    /**
     * Set the current query.
     *
     * @param query The query.
     */
    public void setQuery(String query) {
        this.query = Optional.ofNullable(query);
    }

    @Override
    public BeanItemContainer<Buerger> getSelectedBuergerPartner() {
        return selectedBuergerPartner;
    }


    @Override
    public BeanItemContainer<Buerger> getSelectedBuergerKinder() {
        return selectedBuergerKinder;
    }
}

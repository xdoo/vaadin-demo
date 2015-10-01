package de.muenchen.vaadin.services.model;

import de.muenchen.vaadin.demo.api.local.Buerger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Provides a simple Model for the Buergers in the GUI.
 *
 * @author fabian.holtkoetter p.mueller
 * @version 1.0
 */
public class BuergerModel implements BuergerReadOnlyModel {
    /** The current (single or none) selected Buerger in the GUI. */
    private Optional<Buerger> selectedBuerger = Optional.empty();
    /** All the kinders of the selectedBuerger. */
    private List<Buerger> selectedBuergerKinder = new ArrayList<>();
    /** All the partners of the selectedBuerger. */
    private List<Buerger> selectedBuergerPartner = new ArrayList<>();
    /** A List of all the Buergers, possible reduced by the query. */
    private List<Buerger> buergers;
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
    public List<Buerger> getBuergers() {
        return buergers;
    }

    /**
     * Set the buergers.
     *
     * @param buergers The buergers.
     */
    public void setBuergers(List<Buerger> buergers) {
        this.buergers = buergers;
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
    public List<Buerger> getSelectedBuergerPartner() {
        return selectedBuergerPartner;
    }

    /**
     * Set the partners of the selected buerger.
     *
     * @param selectedBuergerPartner A list of the partners.
     */
    public void setSelectedBuergerPartner(List<Buerger> selectedBuergerPartner) {
        this.selectedBuergerPartner = selectedBuergerPartner;
    }

    @Override
    public List<Buerger> getSelectedBuergerKinder() {
        return selectedBuergerKinder;
    }

    /**
     * Set the kinders of the buerger.
     *
     * @param selectedBuergerKinder A list of the kinders.
     */
    public void setSelectedBuergerKinder(List<Buerger> selectedBuergerKinder) {
        this.selectedBuergerKinder = selectedBuergerKinder;
    }
}

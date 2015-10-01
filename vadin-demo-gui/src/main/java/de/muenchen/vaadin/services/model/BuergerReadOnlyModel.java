package de.muenchen.vaadin.services.model;

import de.muenchen.vaadin.demo.api.local.Buerger;

import java.util.List;
import java.util.Optional;

/**
 * Provides a simple read-only view on the {@link BuergerModel}.
 * @author fabian.holtkoetter
 * @version 1.0
 */
public interface BuergerReadOnlyModel {

    /**
     * Get the current selected Buerger or if none is set an empty Optional.
     *
     * @return The optional buerger.
     */
    Optional<Buerger> getSelectedBuerger();

    /**
     * Get the List of all the buergers.
     *
     * @return A list of all buergers.
     */
    List<Buerger> getBuergers();

    /**
     * Get the current query.
     * @return The current query.
     */
    Optional<String> getQuery();

    /**
     * Get all the partners of the selected buerger.
     * @return A list of the partners.
     */
    List<Buerger> getSelectedBuergerPartner();

    /**
     * Get all the kinders of the selected buerger.
     * @return A list of the kinders.
     */
    List<Buerger> getSelectedBuergerKinder();
}

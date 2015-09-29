package de.muenchen.vaadin.services.model;

import de.muenchen.vaadin.demo.api.local.Buerger;

import java.util.List;
import java.util.Optional;

/**
 * Created by claus.straube on 29.09.15.
 * fabian.holtkoetter ist unschuldig.
 */
public interface BuergerReadOnlyModel {

    Optional<Buerger> getSelectedBuerger();

    List<Buerger> getBuerger();

    Optional<String> getQuery();

    List<Buerger> getSelectedBuergerPartner();

    List<Buerger> getSelectedBuergerKinder();
}

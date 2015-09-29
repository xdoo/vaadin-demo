package de.muenchen.vaadin.services.model;

import de.muenchen.vaadin.demo.api.local.Buerger;

import java.util.List;
import java.util.Map;

/**
 * Created by claus.straube on 29.09.15.
 * fabian.holtkoetter ist unschuldig.
 */
public interface BuergerReadOnlyModel {

    public Buerger getSelectedBuerger();

    public Map<String, List<Buerger>> getSelectedBuergerAssociations();

    public List<Buerger> getBuerger();
}

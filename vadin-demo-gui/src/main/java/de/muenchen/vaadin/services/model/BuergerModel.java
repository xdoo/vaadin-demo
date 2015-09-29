package de.muenchen.vaadin.services.model;

import de.muenchen.vaadin.demo.api.local.Buerger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by claus.straube on 29.09.15.
 * fabian.holtkoetter ist unschuldig.
 */
public class BuergerModel implements BuergerReadOnlyModel{
    private Optional<Buerger> selectedBuerger;

    private Map<String, List<Buerger>> selectedBuergerAssociations;

    private List<Buerger> buerger;

    private Optional<String> query = Optional.empty();

    @Override
    public Optional<Buerger> getSelectedBuerger() {
        return selectedBuerger;
    }

    public void setSelectedBuerger(Buerger selectedBuerger) {
        this.selectedBuerger = Optional.ofNullable(selectedBuerger);
    }

    @Override
    public Map<String, List<Buerger>> getSelectedBuergerAssociations() {
        return selectedBuergerAssociations;
    }

    public void setSelectedBuergerAssociations(Map<String, List<Buerger>> selectedBuergerAssociations) {
        this.selectedBuergerAssociations = selectedBuergerAssociations;
    }

    @Override
    public List<Buerger> getBuerger() {
        return buerger;
    }

    public void setBuerger(List<Buerger> buerger) {
        this.buerger = buerger;
    }

    @Override
    public Optional<String> getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = Optional.ofNullable(query);
    }
}

package de.muenchen.vaadin.services.model;

import com.vaadin.ui.ListSelect;
import de.muenchen.vaadin.demo.api.local.Buerger;

import java.util.List;
import java.util.Map;

/**
 * Created by claus.straube on 29.09.15.
 * fabian.holtkoetter ist unschuldig.
 */
public class BuergerModel implements BuergerReadOnlyModel{
    private Buerger selectedBuerger;

    private Map<String, List<Buerger>> selectedBuergerAssociations;

    private List<Buerger> buerger;

    public Buerger getSelectedBuerger() {
        return selectedBuerger;
    }

    public void setSelectedBuerger(Buerger selectedBuerger) {
        this.selectedBuerger = selectedBuerger;
    }

    public Map<String, List<Buerger>> getSelectedBuergerAssociations() {
        return selectedBuergerAssociations;
    }

    public void setSelectedBuergerAssociations(Map<String, List<Buerger>> selectedBuergerAssociations) {
        this.selectedBuergerAssociations = selectedBuergerAssociations;
    }

    public List<Buerger> getBuerger() {
        return buerger;
    }

    public void setBuerger(List<Buerger> buerger) {
        this.buerger = buerger;
    }
}

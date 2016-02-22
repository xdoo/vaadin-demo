package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.services.model;

import de.muenchen.vaadin.guilib.util.Datastore;
import com.vaadin.data.util.BeanItemContainer;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Pass_;
import java.util.Optional;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
/**
 * Provides a simple Model for the Pass_ in the GUI.
 */
public class Pass_Datastore implements Datastore<Pass_>{
    
    /** A List of all the Pass_, possible reduced by the query. */
    private final BeanItemContainer<Pass_> passs = new BeanItemContainer<>(Pass_.class);
    
    /** The current (single or none) selected pass in the GUI. */
    private Optional<Pass_> selectedPass= Optional.empty();
    
    /** The query to filter the pass. */
    private Optional<String> query = Optional.empty();

    public Optional<Pass_> getSelectedPass() {
        return selectedPass;
    }

    /**
     * Set the Selected Pass, if null the Optional will be empty.
     *
     * @param selectedPass The Pass_ to set as the selected one.
     */
    public void setSelectedPass(Pass_ selectedPass) {
        this.selectedPass = Optional.ofNullable(selectedPass);
    }

    public BeanItemContainer<Pass_> getPasss() {
        return passs;
    }

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
	public BeanItemContainer<Pass_> getEntityContainer() {
		return getPasss();
	}
}


package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.sachbearbeiter;

import de.muenchen.eventbus.events.Association;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;
import de.muenchen.vaadin.guilib.components.actions.EntityAssociationListAction;
import java.util.List;
import java.util.function.Supplier;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Sachbearbeiter_AssociationListActions extends EntityAssociationListAction<Sachbearbeiter_> {
	
	/**
	 * Create new AssociationActions for the Entity with the single association.
	 *
	 * @param association The association.
	 */
	public Sachbearbeiter_AssociationListActions(Supplier<List<Association<?>>> association) {
		super(association, Sachbearbeiter_.class);
	}
}

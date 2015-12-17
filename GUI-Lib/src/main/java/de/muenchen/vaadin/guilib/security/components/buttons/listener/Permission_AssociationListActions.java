package de.muenchen.vaadin.guilib.security.components.buttons.listener;

import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.apilib.local.Permission_;
import de.muenchen.vaadin.guilib.components.actions.EntityAssociationListAction;
import java.util.List;
import java.util.function.Supplier;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Permission_AssociationListActions extends EntityAssociationListAction<Permission_> {
	
	/**
	 * Create new AssociationActions for the Entity with the single association.
	 *
	 * @param association The association.
	 */
	public Permission_AssociationListActions(Supplier<List<Association<?>>> association) {
		super(association, Permission_.class);
	}
}

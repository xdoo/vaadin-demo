package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.buerger;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;

import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;

import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_AssociationActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.buerger.Buerger_CreateForm;

import java.util.Optional;

/**
 * Provides a simple Form for creating a new Buerger_ as an Association.
 *
 * @author claus.straube p.mueller
 * @version 2.0
 */
public class Buerger_Partner_CreateForm extends Buerger_CreateForm {

    /** The relation this CreateForm is for. */
    private final String relation;

    /**
     * Formular zum Erstellen eines {@link Buerger_}s. Über diesen Konstruktor kann zusätzlich eine Zielseite für die
     * 'abbrechen' Schaltfläche erstellt werden. Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in
     * eine definierte Abfolge von Formularen eingebettet wird.
     *
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     * @param relation Angabe einer Assoziation, für die der Buerger_ ist.
     */
    public Buerger_Partner_CreateForm(final String navigateTo, final String relation) {
        super(navigateTo);
        getSaveButton().addActionPerformer(new NavigateActions(navigateTo)::navigate);
        this.relation = relation;
    }
	
	@Override
	protected void configureSaveButton() {
		final Buerger_AssociationActions buergerAssociationActions = new Buerger_AssociationActions(
			() -> new Association<>(getBuerger(), getRelationToCreate()));
		getSaveButton().addActionPerformer(buergerAssociationActions::addAssociation);
		
		getSaveButton().useNotification(true);
	}

    /**
     * Get the Relation this CreateForm is for.
     * @return The relation.
     */
    public String getRelationToCreate() {
        return relation;
    }
}

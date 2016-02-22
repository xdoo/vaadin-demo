package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.wohnung;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Adresse_;

import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;

import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.wohnung.Wohnung_AssociationActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.adresse.Adresse_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.adresse.Adresse_CreateForm;

import java.util.Optional;

/**
 * Provides a simple Form for creating a new Adresse_ as an Association.
 *
 * @author claus.straube p.mueller
 * @version 2.0
 */
public class Wohnung_Adresse_CreateForm extends Adresse_CreateForm {

    /** The relation this CreateForm is for. */
    private final String relation;

    /**
     * Formular zum Erstellen eines {@link Adresse_}s. Über diesen Konstruktor kann zusätzlich eine Zielseite für die
     * 'abbrechen' Schaltfläche erstellt werden. Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in
     * eine definierte Abfolge von Formularen eingebettet wird.
     *
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     * @param relation Angabe einer Assoziation, für die der Adresse_ ist.
     */
    public Wohnung_Adresse_CreateForm(final String navigateTo, final String relation) {
        super(navigateTo);
        getSaveButton().addActionPerformer(new NavigateActions(navigateTo)::navigate);
        this.relation = relation;
    }
	
	@Override
	protected void configureSaveButton() {
		final Wohnung_AssociationActions wohnungAssociationActions = new Wohnung_AssociationActions(
			() -> new Association<>(getAdresse(), getRelationToCreate()));
		getSaveButton().addActionPerformer(wohnungAssociationActions::addAssociation);
		
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

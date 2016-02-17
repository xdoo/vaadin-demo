package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.buerger;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_AssociationListActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Buerger_ViewController;

import java.util.stream.Collectors;

/**
 * @author claus
 */
public class Buerger_Sachbearbeiter_ReadEditGrid extends CustomComponent {

    private Buerger_ViewController controller;
    private GenericGrid<Sachbearbeiter_> grid;
	/**
	 * @param controller
	 * @param navigateToRead detail button will not be shown if null
	 * @param navigateToCreate create button will not be shown if null
	 * @param navigateToAdd add button will not be shown if null
	 */
    public Buerger_Sachbearbeiter_ReadEditGrid(Buerger_ViewController controller, String navigateToRead, String navigateToCreate, String navigateToAdd) {

        this.controller = controller;

        grid = new GenericGrid<Sachbearbeiter_>(controller.getModel().getSelectedBuergerSachbearbeiter(), Sachbearbeiter_.Field.getProperties());
        grid.activateSearch(false);
        
        if(navigateToCreate != null)
        	grid.activateCreate(navigateToCreate);
        NavigateActions navigateActions = new NavigateActions((navigateToAdd == null)? "" : navigateToAdd);
		if(navigateToAdd != null){
	        ActionButton addButton = new ActionButton(Buerger_.class, SimpleAction.add);
	        addButton.addActionPerformer(navigateActions::navigate);
	        grid.addComponent(addButton);
        }
        if(navigateToRead != null)
			grid.activateRead(navigateToRead).activateDoubleClickToRead(navigateToRead);

        //Create Button to delete one or more associations
        ActionButton deleteButton = new ActionButton(Buerger_.class, SimpleAction.delete);
        Buerger_AssociationListActions listAction = new Buerger_AssociationListActions(
                () -> grid.getSelectedEntities().stream()
                        .map(sachbearbeiter -> new Association<>(sachbearbeiter, Buerger_.Rel.sachbearbeiter.name()))
                        .collect(Collectors.toList())
        );
        deleteButton.addActionPerformer(listAction::removeAssociations);
        deleteButton.useNotification(true);
        grid.addMultiSelectComponent(deleteButton);

        HorizontalLayout layout = new HorizontalLayout(grid);
        layout.setSizeFull();
        setCompositionRoot(layout);
	}
	
    public Component addButton(ActionButton button){
    	grid.addComponent(button);
    	return this;
    }
	
	/**
	 * Aktualisiert die Relationen in dieser Grid vom Server. Sollte in der init-Methode der View aufgerufen werden um einen
	 * Konsistenten Datenstand zu gewährleisten.
	 */
	public void reload(){
	    final Buerger_SingleActions singleActions = new Buerger_SingleActions(controller.getModel().getSelectedBuerger()::get);
	    singleActions.reRead(null);
	}
}

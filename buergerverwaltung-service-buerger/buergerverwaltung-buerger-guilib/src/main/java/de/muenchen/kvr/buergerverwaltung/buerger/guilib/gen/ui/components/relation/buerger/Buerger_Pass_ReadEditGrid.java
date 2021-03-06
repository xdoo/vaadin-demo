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
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Pass_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_AssociationListActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Buerger_ViewController;

import java.util.stream.Collectors;

/**
 * @author claus
 */
public class Buerger_Pass_ReadEditGrid extends CustomComponent {

    private Buerger_ViewController controller;
    private GenericGrid<Pass_> grid;
	/**
	 * @param controller
	 * @param navigateToRead detail button will not be shown if null
	 * @param navigateToCreate create button will not be shown if null
	 * @param navigateToAdd add button will not be shown if null
	 */
    public Buerger_Pass_ReadEditGrid(Buerger_ViewController controller, String navigateToRead, String navigateToCreate, String navigateToAdd, String navigateToDelete) {

        this.controller = controller;

        grid = new GenericGrid<Pass_>(controller.getModel().getSelectedBuergerPass(), Pass_.Field.getProperties());
        grid.activateSearch(false);
        
        if(navigateToCreate != null)
        	grid.activateCreate(navigateToCreate);
		if(navigateToAdd != null){
	        ActionButton addButton = new ActionButton(Buerger_.class, SimpleAction.add);
        	NavigateActions addNavigation = new NavigateActions(navigateToAdd);
	        addButton.addActionPerformer(addNavigation::navigate);
	        grid.addComponent(addButton);
        }
        if(navigateToRead != null)
			grid.activateRead(navigateToRead).activateDoubleClickToRead(navigateToRead);
		if(navigateToDelete!=null){
	        //Create Button to delete one or more associations
	        ActionButton deleteButton = new ActionButton(Buerger_.class, SimpleAction.delete);
	        Buerger_AssociationListActions listAction = new Buerger_AssociationListActions(
	                () -> grid.getSelectedEntities().stream()
	                        .map(pass -> new Association<>(pass, Buerger_.Rel.pass.name()))
	                        .collect(Collectors.toList())
	        );
	        deleteButton.addActionPerformer(listAction::removeAssociations);
	        
	        NavigateActions deleteNavigation = new NavigateActions(navigateToDelete);
	        deleteButton.addActionPerformer(deleteNavigation::navigate);
	        deleteButton.useNotification(true);
	        grid.addMultiSelectComponent(deleteButton);
		}
		
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

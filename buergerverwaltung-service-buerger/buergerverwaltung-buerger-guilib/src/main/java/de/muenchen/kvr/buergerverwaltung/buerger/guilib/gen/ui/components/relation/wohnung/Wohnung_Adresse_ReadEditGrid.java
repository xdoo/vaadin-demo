package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.wohnung;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Wohnung_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Adresse_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.wohnung.Wohnung_AssociationListActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.wohnung.Wohnung_SingleActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Wohnung_ViewController;

import java.util.stream.Collectors;

/**
 * @author claus
 */
public class Wohnung_Adresse_ReadEditGrid extends CustomComponent {

    private Wohnung_ViewController controller;
    private GenericGrid<Adresse_> grid;
	/**
	 * @param controller
	 * @param navigateToRead detail button will not be shown if null
	 * @param navigateToCreate create button will not be shown if null
	 * @param navigateToAdd add button will not be shown if null
	 */
    public Wohnung_Adresse_ReadEditGrid(Wohnung_ViewController controller, String navigateToRead, String navigateToCreate, String navigateToAdd, String navigateToDelete) {

        this.controller = controller;

        grid = new GenericGrid<Adresse_>(controller.getModel().getSelectedWohnungAdresse(), Adresse_.Field.getProperties());
        grid.activateSearch(false);
        
        if(navigateToCreate != null)
        	grid.activateCreate(navigateToCreate);
		if(navigateToAdd != null){
	        ActionButton addButton = new ActionButton(Wohnung_.class, SimpleAction.add);
        	NavigateActions addNavigation = new NavigateActions(navigateToAdd);
	        addButton.addActionPerformer(addNavigation::navigate);
	        grid.addComponent(addButton);
        }
        if(navigateToRead != null)
			grid.activateRead(navigateToRead).activateDoubleClickToRead(navigateToRead);
		if(navigateToDelete!=null){
	        //Create Button to delete one or more associations
	        ActionButton deleteButton = new ActionButton(Wohnung_.class, SimpleAction.delete);
	        Wohnung_AssociationListActions listAction = new Wohnung_AssociationListActions(
	                () -> grid.getSelectedEntities().stream()
	                        .map(adresse -> new Association<>(adresse, Wohnung_.Rel.adresse.name()))
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
	    final Wohnung_SingleActions singleActions = new Wohnung_SingleActions(controller.getModel().getSelectedWohnung()::get);
	    singleActions.reRead(null);
	}
}

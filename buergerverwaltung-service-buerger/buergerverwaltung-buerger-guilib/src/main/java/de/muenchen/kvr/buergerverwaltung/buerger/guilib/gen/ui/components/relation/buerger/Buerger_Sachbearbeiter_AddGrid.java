package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.buerger;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;

import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_AssociationActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_AssociationListActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Sachbearbeiter_ViewController;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.sachbearbeiter.Sachbearbeiter_Grid;

import java.util.stream.Collectors;

public class Buerger_Sachbearbeiter_AddGrid extends BaseComponent {
    private final String navigateOnAdd;
    private GenericGrid<Sachbearbeiter_> grid;
    private final Sachbearbeiter_ViewController controller;

    public Buerger_Sachbearbeiter_AddGrid(Sachbearbeiter_ViewController controller, String navigateOnAdd){
        this.controller = controller;
        this.navigateOnAdd= navigateOnAdd;
        init();
    }

    protected void init(){
        final NavigateActions navigateActions = new NavigateActions(navigateOnAdd);

        grid = new Sachbearbeiter_Grid(controller);

		grid.setSelectionMode(Grid.SelectionMode.MULTI).activateSearch();

        ActionButton addButton = new ActionButton(Sachbearbeiter_.class, SimpleAction.add);
        
        Buerger_AssociationListActions actionMultiple = new Buerger_AssociationListActions(
                () -> grid.getSelectedEntities().stream().map(entity -> new Association<>(entity, Buerger_.Rel.sachbearbeiter.name())).collect(Collectors.toList()));
        addButton.addActionPerformer(actionMultiple::addAssociations);
        addButton.addActionPerformer(navigateActions::navigate);
        addButton.useNotification(true);
        grid.addMultiSelectComponent(addButton);

        setCompositionRoot(grid);
    }
    
    public Component addButton(ActionButton button){
    	grid.addComponent(button);
    	return this;
    }
}


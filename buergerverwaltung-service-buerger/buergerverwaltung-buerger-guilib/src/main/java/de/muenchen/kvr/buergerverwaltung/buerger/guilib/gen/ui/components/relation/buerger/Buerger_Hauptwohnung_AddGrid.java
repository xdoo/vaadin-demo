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
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Wohnung_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_AssociationActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.buerger.Buerger_AssociationListActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Wohnung_ViewController;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.wohnung.Wohnung_Grid;

import java.util.stream.Collectors;

public class Buerger_Hauptwohnung_AddGrid extends BaseComponent {
    private final String navigateOnAdd;
    private GenericGrid<Wohnung_> grid;
    private final Wohnung_ViewController controller;

    public Buerger_Hauptwohnung_AddGrid(Wohnung_ViewController controller, String navigateOnAdd){
        this.controller = controller;
        this.navigateOnAdd= navigateOnAdd;
        init();
    }

    protected void init(){
        final NavigateActions navigateActions = new NavigateActions(navigateOnAdd);

        grid = new Wohnung_Grid(controller);

		grid.setSelectionMode(Grid.SelectionMode.SINGLE).activateSearch();

        ActionButton addButton = new ActionButton(Wohnung_.class, SimpleAction.add);
        
        Buerger_AssociationActions actionsSingle = new Buerger_AssociationActions(
                () -> new Association<>(grid.getSelectedEntities().get(0), Buerger_.Rel.hauptwohnung.name()));
        addButton.addActionPerformer(actionsSingle::addAssociation);
        addButton.addActionPerformer(navigateActions::navigate);
        addButton.useNotification(true);
        grid.addSingleSelectComponent(addButton);

        setCompositionRoot(grid);
    }
    
    public Component addButton(ActionButton button){
    	grid.addComponent(button);
    	return this;
    }
}


package de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.relation.wohnung;

import com.vaadin.ui.Grid;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;

import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Wohnung_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Adresse_;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.wohnung.Wohnung_AssociationActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.buttons.listener.wohnung.Wohnung_AssociationListActions;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.controller.Adresse_ViewController;
import de.muenchen.kvr.buergerverwaltung.buerger.guilib.gen.ui.components.entity.adresse.Adresse_Grid;

import java.util.stream.Collectors;

public class Wohnung_Adresse_AddGrid extends BaseComponent {
    private final String navigateOnAdd;
    private GenericGrid<Adresse_> grid;
    private final Adresse_ViewController controller;

    public Wohnung_Adresse_AddGrid(Adresse_ViewController controller, String navigateOnAdd){
        this.controller = controller;
        this.navigateOnAdd= navigateOnAdd;
        init();
    }

    protected void init(){
        final NavigateActions navigateActions = new NavigateActions(navigateOnAdd);

        grid = new Adresse_Grid(controller);

		grid.setSelectionMode(Grid.SelectionMode.MULTI).activateSearch();

        ActionButton addButton = new ActionButton(Adresse_.class, SimpleAction.add);
        
        Wohnung_AssociationListActions actionMultiple = new Wohnung_AssociationListActions(
                () -> grid.getSelectedEntities().stream().map(entity -> new Association<>(entity, Wohnung_.Rel.adresse.name())).collect(Collectors.toList()));
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


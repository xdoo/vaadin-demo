package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.Grid;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.demo.apilib.local.Permission_;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.Authority_AssociationListActions;
import de.muenchen.vaadin.guilib.security.controller.Permission_ViewController;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import java.util.stream.Collectors;

public class Authority_Permissions_AddGrid extends BaseComponent {
    private final String navigateOnAdd;
    private final Permission_ViewController controller;

    public Authority_Permissions_AddGrid(Permission_ViewController controller, String navigateOnAdd){
        this.controller = controller;
        this.navigateOnAdd= navigateOnAdd;
        init();
    }

    protected void init(){
        final NavigateActions navigateActions = new NavigateActions(navigateOnAdd);

        final GenericGrid<Permission_> grid = new Permission_Grid(controller);

		grid.setSelectionMode(Grid.SelectionMode.SINGLE).activateSearch();

        ActionButton addButton = new ActionButton(Permission_.class, SimpleAction.add);
        
        Authority_AssociationListActions actionMultiple = new Authority_AssociationListActions(
                () -> grid.getSelectedEntities().stream().map(entity -> new Association<>(entity, Authority_.Rel.permissions.name())).collect(Collectors.toList()));
        addButton.addActionPerformer(actionMultiple::addAssociations);
        addButton.addActionPerformer(navigateActions::navigate);
        grid.addMultiSelectComponent(addButton);

        setCompositionRoot(grid);
    }
}


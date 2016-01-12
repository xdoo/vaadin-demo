package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.Grid;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.apilib.local.Authority;
import de.muenchen.vaadin.demo.apilib.local.User;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.User_AssociationListActions;
import de.muenchen.vaadin.guilib.security.controller.Authority_ViewController;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import java.util.stream.Collectors;

public class User_Authorities_AddGrid extends BaseComponent {
    private final String navigateOnAdd;
    private final Authority_ViewController controller;

    public User_Authorities_AddGrid(Authority_ViewController controller, String navigateOnAdd){
        this.controller = controller;
        this.navigateOnAdd= navigateOnAdd;
        init();
    }

    protected void init(){
        final NavigateActions navigateActions = new NavigateActions(navigateOnAdd);

        final GenericGrid<Authority> grid = new Authority_Grid(controller);

		grid.setSelectionMode(Grid.SelectionMode.SINGLE).activateSearch();

        ActionButton addButton = new ActionButton(Authority.class, SimpleAction.add);
        
        User_AssociationListActions actionMultiple = new User_AssociationListActions(
                () -> grid.getSelectedEntities().stream().map(entity -> new Association<>(entity, User.Rel.authoritys.name())).collect(Collectors.toList()));
        addButton.addActionPerformer(actionMultiple::addAssociations);
        addButton.addActionPerformer(navigateActions::navigate);
        grid.addMultiSelectComponent(addButton);

        setCompositionRoot(grid);
    }
}


package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.demo.apilib.local.Permission_;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.Authority_AssociationListActions;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.Authority_SingleActions;
import de.muenchen.vaadin.guilib.security.controller.Authority_ViewController;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import java.util.stream.Collectors;

/**
 * @author claus
 */
public class Authority_Permissions_ReadEditGrid extends CustomComponent {

    private Authority_ViewController controller;
    private GenericGrid<Permission_> grid;

    public Authority_Permissions_ReadEditGrid(Authority_ViewController controller, String navigateToRead, String navigateToCreate, String navigateToAdd
    ) {

        this.controller = controller;

        grid = new GenericGrid<Permission_>(controller.getModel().getSelectedAuthorityPermissions(), Permission_.Field.getProperties());
        //grid.activateSearch();
        grid.activateCreate(navigateToCreate);

        ActionButton addButton = new ActionButton(Authority_.class, SimpleAction.add);
        NavigateActions navigateActions = new NavigateActions(navigateToAdd);
        addButton.addActionPerformer(navigateActions::navigate);
        grid.addComponent(addButton);

        //Create Button to delete one or more associations
        ActionButton deleteButton = new ActionButton(Authority_.class, SimpleAction.delete);
        Authority_AssociationListActions listAction = new Authority_AssociationListActions(
                () -> grid.getSelectedEntities().stream()
                        .map(permission -> new Association<>(permission, Authority_.Rel.permissions.name()))
                        .collect(Collectors.toList())
        );
        deleteButton.addActionPerformer(listAction::removeAssociations);
        grid.addMultiSelectComponent(deleteButton);

        HorizontalLayout layout = new HorizontalLayout(grid);
        layout.setSizeFull();
        setCompositionRoot(layout);
	}

	/**
	 * Aktualisiert die Relationen in dieser Grid vom Server. Sollte in der init-Methode der View aufgerufen werden um einen
	 * Konsistenten Datenstand zu gewährleisten.
	 */
	public void reload(){
	    final Authority_SingleActions singleActions = new Authority_SingleActions(controller.getModel().getSelectedAuthority()::get);
	    singleActions.reRead(null);
	}
}


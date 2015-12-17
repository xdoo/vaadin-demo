package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.demo.apilib.local.User_;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.User_AssociationListActions;
import de.muenchen.vaadin.guilib.security.controller.User_ViewController;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

import java.util.stream.Collectors;

/**
 * @author claus
 */
public class User_Authoritys_ReadEditGrid extends CustomComponent {

    private User_ViewController controller;
    private GenericGrid<Authority_> grid;

    public User_Authoritys_ReadEditGrid(User_ViewController controller, String navigateToRead, String navigateToCreate, String navigateToAdd
    ) {

        this.controller = controller;

        grid = new GenericGrid<Authority_>(controller.getModel().getSelectedUserAuthorities(), Authority_.Field.getProperties());
        grid.activateCreate(navigateToCreate).activateRead(navigateToRead);

        ActionButton addButton = new ActionButton(User_.class, SimpleAction.add);
        NavigateActions navigateActions = new NavigateActions(navigateToAdd);
        addButton.addActionPerformer(navigateActions::navigate);
        grid.addComponent(addButton);

        //Create Button to delete one or more associations
        ActionButton deleteButton = new ActionButton(User_.class, SimpleAction.delete);
        User_AssociationListActions listAction = new User_AssociationListActions(
                () -> grid.getSelectedEntities().stream()
                        .map(authority -> new Association<>(authority, User_.Rel.authoritys.name()))
                        .collect(Collectors.toList())
        );
        deleteButton.addActionPerformer(listAction::removeAssociations);
        grid.addMultiSelectComponent(deleteButton);

        HorizontalLayout layout = new HorizontalLayout(grid);
        layout.setSizeFull();
        layout.setMargin(true);
        setCompositionRoot(layout);
	}

}


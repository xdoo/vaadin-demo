package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.Authority_SingleActions;
import de.muenchen.vaadin.guilib.security.controller.Authority_ViewController;
import de.muenchen.vaadin.guilib.security.controller.Permission_ViewController;

/**
 * Created by maximilian.zollbrecht on 17.12.15.
 */
public class Authority_Permissions_ReadEdit extends BaseComponent {

    private final Authority_ViewController authorityViewController;
    private final Permission_ViewController permissionViewController;
    private final String navigateToCreateAuthority;

    private final HorizontalLayout layout = new HorizontalLayout();

    private final Authority_Grid authGrid;
    private final Authority_Permissions_TwinSelect permSelect;

    public Authority_Permissions_ReadEdit(Permission_ViewController permissionViewController, Authority_ViewController authorityViewController, String navigateToCreateAuthority) {
        this.navigateToCreateAuthority = navigateToCreateAuthority;
        this.permissionViewController = permissionViewController;
        this.authorityViewController = authorityViewController;

        permSelect = new Authority_Permissions_TwinSelect(permissionViewController);
        authGrid = new Authority_Grid(authorityViewController);

        init();
    }

    private void init() {
        permSelect.setEnabled(false);

        authGrid.activateCreate(navigateToCreateAuthority);
        authGrid.activateDelete();

        final Authority_SingleActions authoritySingleActions = new Authority_SingleActions(authGrid::getSelectedEntity);
        authGrid.addSelectionListener(event -> {
            final boolean isOneSelected = authGrid.getSelectedEntities().size() == 1;

            // enable/disable save-button for permissions
            permSelect.setEnabled(isOneSelected);

            // deselect all Permissions
            permSelect.deselectAll();

            if (isOneSelected) {
                // select authorities' permissions
                authoritySingleActions.read(null);
                permSelect.select(authorityViewController.getModel().getSelectedAuthorityPermissions());
            }
        });

        // Add components to the default layout
        layout.addComponents(authGrid, permSelect);
        layout.setSizeFull();
        setSizeFull();
        layout.setExpandRatio(authGrid, 1.5f);
        layout.setExpandRatio(permSelect, 2);
        layout.setSpacing(true);
        setCompositionRoot(layout);
    }
}

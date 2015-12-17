package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolverImpl;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.app.views.BuergerAddChildView;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationListActions;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import java.util.stream.Collectors;

/**
 * @author claus
 */
@SuppressWarnings("unchecked")
public class BuergerChildTab extends CustomComponent {

    private BuergerViewController controller;
    private GenericGrid<Buerger> grid;

    public BuergerChildTab(BuergerViewController controller, I18nResolverImpl resolver, String navigateToForDetail, String navigateToForCreate, String navigateBack) {

        this.controller = controller;

        grid = this.controller.getViewFactory().generateChildTable(BuergerDetailView.NAME)
                .activateCreate(navigateToForCreate)
                .activateRead(BuergerDetailView.NAME);

        ActionButton addButton = new ActionButton(Buerger.class, SimpleAction.add);
        NavigateActions navigateActions = new NavigateActions(BuergerAddChildView.NAME);
        addButton.addActionPerformer(navigateActions::navigate);
        grid.addComponent(addButton);

        //Create Button to delete one or more associations
        ActionButton deleteButton = new ActionButton(Buerger.class, SimpleAction.delete);
        BuergerAssociationListActions listAction = new BuergerAssociationListActions(
                () -> grid.getSelectedEntities().stream()
                        .map(buerger -> new Association<>(buerger, Buerger.Rel.kinder.name()))
                        .collect(Collectors.toList())
        );
        deleteButton.addActionPerformer(listAction::removeAssociations);
        grid.addMultiSelectComponent(deleteButton);


        HorizontalLayout layout = new HorizontalLayout(grid);
        layout.setSizeFull();
        layout.setMargin(true);
        setCompositionRoot(layout);


        setId(String.format("%s_%s_%s_CHILD_TAB", navigateToForDetail, navigateBack, controller.getResolver().getBasePath(Buerger.class)));
    }


}

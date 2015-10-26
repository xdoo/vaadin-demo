package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.I18nResolverImpl;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationListActions;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import java.util.stream.Collectors;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;

/**
 * @author claus
 */
@SuppressWarnings("unchecked")
public class BuergerChildTab extends CustomComponent {

    private BuergerViewController controller;
    private GenericGrid<Buerger> grid;

    public BuergerChildTab(BuergerViewController controller, I18nResolverImpl resolver, String navigateToForDetail, String navigateToForCreate, String navigateBack) {

        this.controller = controller;

        grid = controller.getViewFactory().generateChildTable(BuergerDetailView.NAME)
                .activateCreate(navigateToForCreate)
                .activateRead(BuergerDetailView.NAME)
                .addButton(
                        controller.getResolver().resolveRelative(Buerger.class,
                                getFormPath(SimpleAction.add,
                                        I18nPaths.Component.button,
                                        I18nPaths.Type.label)),
                        () -> {
                            HorizontalLayout layout = new HorizontalLayout(controller.getViewFactory().generateChildSearchTable());
                            layout.setMargin(true);
                            getUI().addWindow(new TableSelectWindow(controller, controller.getResolver(), layout));
                        });


        //Create Button to delete one or more associations
        ActionButton deleteButton = new ActionButton(resolver, SimpleAction.delete);
        BuergerAssociationListActions listAction = new BuergerAssociationListActions(resolver,
                () -> grid.getSelectedEntities().stream()
                        .map(buerger -> new Association<>( buerger, Buerger.Rel.kinder.name()))
                        .collect(Collectors.toList())
        );
        deleteButton.addActionPerformer(listAction::removeAssociations);
        grid.addMultiSelectButton(deleteButton);


        HorizontalLayout layout = new HorizontalLayout(grid);
        layout.setSizeFull();
        layout.setMargin(true);
        setCompositionRoot(layout);


        setId(String.format("%s_%s_%s_CHILD_TAB", navigateToForDetail, navigateBack, controller.getResolver().getBasePath(Buerger.class)));
    }


}

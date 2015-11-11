package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationListActions;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import java.util.stream.Collectors;

/**
 * Created by maximilian.zollbrecht on 28.10.15.
 */
public class BuergerAddPartnerGrid extends BaseComponent {
    private final String navigateOnAdd;
    private final BuergerViewController controller;

    public BuergerAddPartnerGrid(String navigateOnAdd, BuergerViewController controller){
        this.controller = controller;
        this.navigateOnAdd= navigateOnAdd;
        init();
    }

    protected void init(){
        final ActionButton backButton = new ActionButton(Buerger.class, SimpleAction.back);
        final NavigateActions navigateActions = new NavigateActions(navigateOnAdd);
        backButton.addActionPerformer(navigateActions::navigate);

        final GenericGrid<Buerger> grid = this.controller.getViewFactory().generateBuergerSearchTable();

        grid.setSelectionMode(Grid.SelectionMode.SINGLE).activateSearch();

        ActionButton addSingle = new ActionButton(Buerger.class, SimpleAction.add);
        BuergerAssociationActions actionsSingle = new BuergerAssociationActions(
                () -> new Association<>(grid.getSelectedEntities().get(0), Buerger.Rel.partner.name()));
        addSingle.addActionPerformer(actionsSingle::addAssociation);
        addSingle.addActionPerformer(navigateActions::navigate);

        grid.addSingleSelectComponent(addSingle);

        final VerticalLayout layout = new VerticalLayout(backButton, grid);
        layout.setSpacing(true);

        setCompositionRoot(layout);
    }
}

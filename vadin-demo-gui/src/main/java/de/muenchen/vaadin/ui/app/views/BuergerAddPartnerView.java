package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationActions;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by claus.straube on 27.10.15.
 * fabian.holtkoetter ist unschuldig.
 */
@SpringView(name = BuergerAddPartnerView.NAME)
@UIScope
public class BuergerAddPartnerView extends DefaultBuergerView {

    public static final String NAME = "buerger_add_partner_view";

    @Autowired
    public BuergerAddPartnerView(BuergerViewController controller) {
        super(controller);
    }

    @Override
    protected void site() {

        final ActionButton backButton = new ActionButton(Buerger.class, SimpleAction.back);
        final NavigateActions navigateActions = new NavigateActions(BuergerDetailView.NAME);
        backButton.addActionPerformer(navigateActions::navigate);

        final GenericGrid<Buerger> grid = this.controller.getViewFactory().generateBuergerSearchTable();

        grid.getGrid().setSelectionMode(Grid.SelectionMode.SINGLE);

        ActionButton addSingle = new ActionButton(Buerger.class, SimpleAction.add);
        BuergerAssociationActions actionsSingle = new BuergerAssociationActions(
                () -> new Association<>(grid.getSelectedEntities().get(0), Buerger.Rel.partner.name()));
        addSingle.addActionPerformer(actionsSingle::addAssociation);
        addSingle.addActionPerformer(navigateActions::navigate);

        grid.addSingleSelectButton(addSingle);

        final VerticalLayout layout = new VerticalLayout(backButton, grid);
        layout.setSpacing(true);

        addComponent(layout);
    }
}
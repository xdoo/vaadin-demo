package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationListActions;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

/**
 * Created by claus.straube on 27.10.15.
 * fabian.holtkoetter ist unschuldig.
 */
@SpringView(name = BuergerAddChildView.NAME)
@UIScope
public class BuergerAddChildView extends DefaultBuergerView{

    public static final String NAME = "buerger_add_child_view";

    @Autowired
    public BuergerAddChildView(BuergerViewController controller) {
        super(controller);
    }

    @Override
    protected void site() {

        final ActionButton backButton = new ActionButton(Buerger.class, SimpleAction.back);
        final NavigateActions navigateActions = new NavigateActions(BuergerDetailView.NAME);
        backButton.addActionPerformer(navigateActions::navigate);

        final GenericGrid<Buerger> grid = this.controller.getViewFactory().generateBuergerSearchTable();

        ActionButton addMultiple = new ActionButton(Buerger.class, SimpleAction.add);
        BuergerAssociationListActions actionMultiple = new BuergerAssociationListActions(
                () -> grid.getSelectedEntities().stream().map(buerger -> new Association<>(buerger, Buerger.Rel.kinder.name())).collect(Collectors.toList()));
        addMultiple.addActionPerformer(actionMultiple::addAssociations);
        addMultiple.addActionPerformer(navigateActions::navigate);

        grid.addMultiSelectButton(addMultiple);

        final VerticalLayout layout = new VerticalLayout(backButton, grid);
        layout.setSpacing(true);

        addComponent(layout);
    }
}
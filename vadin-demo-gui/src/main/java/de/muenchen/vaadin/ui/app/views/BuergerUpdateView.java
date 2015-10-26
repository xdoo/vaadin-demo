package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.forms.BuergerUpdateForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by p.mueller on 23.10.15.
 */
@SpringView(name = BuergerUpdateView.NAME)
@UIScope
public class BuergerUpdateView extends DefaultBuergerView {

    public static final String NAME = "buerger_update_view";

    @Autowired
    public BuergerUpdateView(BuergerViewController controller, BuergerI18nResolver resolver, MainUI ui) {
        super(controller, resolver, ui);
    }

    @Override
    protected void site() {
        final ActionButton backButton = new ActionButton(controller.getResolver(), SimpleAction.back);
        final NavigateActions navigateActions = new NavigateActions(BuergerDetailView.NAME);
        backButton.addActionPerformer(navigateActions::navigate);

        final BuergerUpdateForm c = new BuergerUpdateForm(controller, BuergerDetailView.NAME);

        final VerticalLayout layout = new VerticalLayout(backButton, c);
        layout.setSpacing(true);

        addComponent(layout);
    }

}

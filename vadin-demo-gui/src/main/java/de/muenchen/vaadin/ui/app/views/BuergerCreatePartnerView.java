package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.forms.BuergerCreateForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = BuergerCreatePartnerView.NAME)
@UIScope
public class BuergerCreatePartnerView extends DefaultBuergerView {

    public static final String NAME = "buerger_create_partner_view";
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerCreatePartnerView.class);

    @Autowired
    public BuergerCreatePartnerView(BuergerViewController controller, BuergerI18nResolver resolver, MainUI ui) {
        super(controller, resolver, ui);
        LOG.debug("creating 'buerger_create_partner_view'");
    }

    @Override
    protected void site() {
        final ActionButton backButton = new ActionButton(controller.getResolver(), SimpleAction.back);
        final NavigateActions navigateActions = new NavigateActions(BuergerDetailView.NAME);
        backButton.addActionPerformer(navigateActions::navigate);

        final BuergerCreateForm c = this.controller.getViewFactory().generateCreatePartnerForm(BuergerDetailView.NAME, BuergerDetailView.NAME);

        final VerticalLayout layout = new VerticalLayout(backButton, c);
        layout.setSpacing(true);

        addComponent(layout);
    }

}

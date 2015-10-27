package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
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
    public BuergerCreatePartnerView(BuergerViewController controller) {
        super(controller);
        LOG.debug("creating 'buerger_create_partner_view'");
    }

    @Override
    protected void site() {
        addComponent(this.controller.getViewFactory().generateCreatePartnerForm(BuergerDetailView.NAME, BuergerDetailView.NAME));
    }

}

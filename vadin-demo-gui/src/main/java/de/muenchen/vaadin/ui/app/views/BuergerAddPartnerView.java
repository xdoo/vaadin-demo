package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.ui.components.BuergerAddPartnerGrid;
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

        final BuergerAddPartnerGrid grid = new BuergerAddPartnerGrid(BuergerDetailView.NAME, controller);

        addComponent(grid);
    }
}
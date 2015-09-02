package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.BuergerPartnerSearchTable;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = BuergerPartnerSelectView.NAME)
@UIScope
public class BuergerPartnerSelectView extends DefaultBuergerView {

    public static final String NAME = "buerger_partner_search_view";
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerCreateView.class);
    private BuergerPartnerSearchTable table;
    @Autowired
    public BuergerPartnerSelectView(BuergerViewController controller, MainUI ui ){
        super(controller, ui);
        LOG.debug("creating 'buerger_table_view'");

    }

    @Override
    protected void site() {

        table= this.controller.getViewFactory().generateBuergerPartnerSearchTable(BuergerDetailView.NAME);
        addComponent(table);
    }



}
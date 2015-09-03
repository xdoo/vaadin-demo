package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by arne.schoentag on 02.09.15.
 */
@SpringView(name = BuergerHistoryView.NAME)
@UIScope
public class BuergerHistoryView  extends DefaultBuergerView {

    public static final String NAME = "buerger_history_view";
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerCreateView.class);

    @Autowired
    public BuergerHistoryView(BuergerViewController controller, MainUI ui) {
        super(controller, ui);
        LOG.debug("creating 'buerger_history_view'");
    }

    @Override
    protected void site() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.addComponent(this.controller.getViewFactory().generateHistoryTable(BuergerTableView.NAME));
        layout.addComponent(this.controller.getViewFactory().generateTable(NAME));
        addComponent(layout);
    }

}


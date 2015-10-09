package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = BuergerCreateView.NAME)
@UIScope
public class BuergerCreateView extends DefaultBuergerView {
    
    public static final String NAME = "buerger_create_view";
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerCreateView.class);

    @Autowired
    public BuergerCreateView(BuergerViewController controller, BuergerI18nResolver resolver, MainUI ui) {
        super(controller, resolver, ui);
        LOG.debug("creating 'buerger_create_view'");
    }

    @Override
    protected void site() {
        addComponent(this.controller.getViewFactory().generateCreateForm(BuergerTableView.NAME));
    }
    
}

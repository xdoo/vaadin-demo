package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
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
        addComponent(new BuergerUpdateForm(controller, BuergerDetailView.NAME));
    }

}

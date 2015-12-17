package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.ui.components.BuergerAddKinderGrid;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by claus.straube on 27.10.15.
 * fabian.holtkoetter ist unschuldig.
 */
@SpringView(name = BuergerAddChildView.NAME)
@UIScope
public class BuergerAddChildView extends DefaultBuergerView {

    public static final String NAME = "buerger_add_child_view";

    @Autowired
    public BuergerAddChildView(BuergerViewController controller) {
        super(controller);
    }

    @Override
    protected void site() {

        final BuergerAddKinderGrid grid = new BuergerAddKinderGrid(BuergerDetailView.NAME, controller);

        addComponent(grid);
    }
}
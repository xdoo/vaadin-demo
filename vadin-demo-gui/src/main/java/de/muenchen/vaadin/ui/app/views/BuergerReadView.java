package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.BuergerReadForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = BuergerReadView.NAME)
@UIScope
public class BuergerReadView extends DefaultBuergerView {

    public static final String NAME = "buerger_read_view";
    
    @Autowired
    public BuergerReadView(BuergerViewController controller, MainUI ui) {
        super(controller, ui);
    }

    @Override
    protected void site() {
        BuergerReadForm readForm = this.controller.generateReadForm(BuergerUpdateView.NAME, BuergerTableView.NAME);
        addComponent(readForm);
    }
    
}

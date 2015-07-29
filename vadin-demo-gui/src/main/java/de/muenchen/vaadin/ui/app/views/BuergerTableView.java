package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.BuergerSearchForm;
import de.muenchen.vaadin.ui.components.CreateBuergerButton;
import de.muenchen.vaadin.ui.components.BuergerTable;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = BuergerTableView.NAME)
@UIScope
public class BuergerTableView extends DefaultBuergerView {
    
    public static final String NAME = "buerger_table_view";
    
    @Autowired
    public BuergerTableView(BuergerViewController controller, MainUI ui) {
        super(controller, ui);
    }

    @Override
    protected void site() {
        CreateBuergerButton create = new CreateBuergerButton(controller, BuergerCreateView.NAME);
        BuergerTable table = this.controller.generateBuergerTable(BuergerUpdateView.NAME, BuergerReadView.NAME);
        BuergerSearchForm search = new BuergerSearchForm(this.controller);
        search.setWidth("100%");
        
        HorizontalLayout hlayout = new HorizontalLayout(search, create);
        hlayout.setSpacing(true);
        hlayout.setExpandRatio(search, 0.7F);
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);
        
        addComponent(vlayout);
    }
    
}

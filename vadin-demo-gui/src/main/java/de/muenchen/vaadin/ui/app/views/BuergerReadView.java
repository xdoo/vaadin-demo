package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.BuergerChildTabSheet;
import de.muenchen.vaadin.ui.components.BuergerChildTab;
import de.muenchen.vaadin.ui.components.BuergerReadForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author claus
 */
@SpringView(name = BuergerReadView.NAME)
@UIScope
public class BuergerReadView extends DefaultBuergerView {

    public static final String NAME = "buerger_read_view";
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerReadView.class);
    
    @Autowired
    public BuergerReadView(BuergerViewController controller, MainUI ui) {
        super(controller, ui);
        LOG.debug("creating 'buerger_read_view'");
    }

    @Override
    protected void site() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        
        // read form
        BuergerReadForm readForm = this.controller.generateReadForm(BuergerUpdateView.NAME, this.NAME);
        layout.addComponent(readForm);
        
        // tab sheet
        TabSheet tabSheet = new TabSheet();
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        
        // add kind tab
        TabSheet.Tab kindTab = tabSheet.addTab(new BuergerChildTab(controller, BuergerUpdateView.NAME, BuergerReadView.NAME, BuergerCreateChildView.NAME, NAME));
        kindTab.setCaption("Kinder"); // TODO -> i18n
        
        layout.addComponent(tabSheet);
        
        addComponent(layout);
    }
    
}

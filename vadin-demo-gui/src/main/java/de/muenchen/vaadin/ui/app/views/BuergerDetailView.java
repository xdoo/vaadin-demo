package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.ui.app.MainUI;
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
@SpringView(name = BuergerDetailView.NAME)
@UIScope
public class BuergerDetailView extends DefaultBuergerView {

    public static final String NAME = "buerger_read_view";
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerDetailView.class);
    private BuergerChildTab childTab;
    private BuergerReadForm readForm;
    @Autowired
    public BuergerDetailView(BuergerViewController controller, MainUI ui) {
        super(controller, ui);
        LOG.debug("creating 'buerger_read_view'");
    }

    @Override
    protected void site() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        
        // read form
        readForm = this.controller.generateReadForm(BuergerUpdateView.NAME, NAME);
        layout.addComponent(readForm);
        
        // tab sheet
        TabSheet tabSheet = new TabSheet();
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        
        // add kind tab
        childTab=controller.generateChildTab(BuergerDetailView.NAME, BuergerCreateChildView.NAME, NAME);
        TabSheet.Tab kindTab = tabSheet.addTab(childTab);
        kindTab.setCaption("Kinder"); // TODO -> i18n
        
        layout.addComponent(tabSheet);
        
        addComponent(layout);
    }
    public void unRegister(){
        controller.getEventbus().unregister(childTab.getTable());
        controller.getEventbus().unregister(childTab);
        controller.getEventbus().unregister(readForm);
    }
}

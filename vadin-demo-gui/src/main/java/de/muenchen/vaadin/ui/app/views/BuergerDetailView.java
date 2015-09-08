package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.BuergerChildTab;
import de.muenchen.vaadin.ui.components.BuergerPartnerTab;
import de.muenchen.vaadin.ui.components.BuergerReadForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.I18nPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static de.muenchen.vaadin.ui.util.I18nPaths.getEntityFieldPath;

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
    private BuergerPartnerTab partnerTab;
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
        readForm = this.controller.getViewFactory().generateReadForm(BuergerUpdateView.NAME, NAME);
        layout.addComponent(readForm);
        
        // tab sheet
        TabSheet tabSheet = new TabSheet();
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        
        // add kind tab
        childTab=controller.getViewFactory().generateChildTab(BuergerDetailView.NAME, BuergerCreateChildView.NAME, NAME);
        TabSheet.Tab kindTab = tabSheet.addTab(childTab);
        kindTab.setCaption(controller.resolveRelative(getEntityFieldPath(Buerger.KINDER, I18nPaths.Type.label)));


        partnerTab = controller.getViewFactory().generatePartnerTab(BuergerDetailView.NAME, BuergerCreatePartnerView.NAME, null, NAME);
        TabSheet.Tab pTab = tabSheet.addTab(partnerTab);
        pTab.setCaption(controller.resolveRelative(getEntityFieldPath(Buerger.PARTNER, I18nPaths.Type.label)));
        layout.addComponent(tabSheet);
        
        addComponent(layout);
    }

}

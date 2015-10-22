package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.ui.app.MainUI;
import de.muenchen.vaadin.ui.components.BuergerChildTab;
import de.muenchen.vaadin.ui.components.BuergerPartnerTab;
import de.muenchen.vaadin.ui.components.forms.BuergerReadForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;

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
    public BuergerDetailView(BuergerViewController controller, BuergerI18nResolver resolver, MainUI ui) {
        super(controller, resolver, ui);
        LOG.debug("creating 'buerger_read_view'");
    }

    @Override
    protected void site() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        // read form
        readForm = this.controller.getViewFactory().generateReadForm(BuergerUpdateView.NAME, BuergerTableView.NAME);
        layout.addComponent(readForm);

        // tab sheet
        TabSheet tabSheet = new TabSheet();
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);

        // add kind tab
        childTab = controller.getViewFactory().generateChildTab(BuergerDetailView.NAME, BuergerCreateChildView.NAME, BuergerTableView.NAME);
        TabSheet.Tab kindTab = tabSheet.addTab(childTab);
        kindTab.setCaption(resolver.resolveRelative(getEntityFieldPath(Buerger.Rel.kinder.name(), I18nPaths.Type.label)));

        partnerTab = controller.getViewFactory().generatePartnerTab(BuergerDetailView.NAME, BuergerCreatePartnerView.NAME, NAME);
        TabSheet.Tab pTab = tabSheet.addTab(partnerTab);
        pTab.setCaption(resolver.resolveRelative(getEntityFieldPath(Buerger.Rel.partner.name(), I18nPaths.Type.label)));
        layout.addComponent(tabSheet);

        addComponent(layout);
    }

}

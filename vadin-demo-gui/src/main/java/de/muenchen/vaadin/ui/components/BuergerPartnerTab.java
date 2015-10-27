package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolverImpl;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.ui.components.forms.BuergerPartnerForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Maximilian Schug
 */
public class BuergerPartnerTab extends CustomComponent {

    private BuergerPartnerForm component;

    public BuergerPartnerTab(BuergerViewController controller, String navigateToForDetail, String navigateToForCreate, String from) {
        component = controller.getViewFactory().generateBuergerPartnerComponent(navigateToForCreate, navigateToForDetail);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(component);
        vlayout.setSpacing(true);
        vlayout.setMargin(true);

        setId(String.format("%s_%s_%s_PARENT_TAB", navigateToForDetail, from, BaseUI.getCurrentI18nResolver().getBasePath(Buerger.class)));
        setCompositionRoot(vlayout);
    }
}

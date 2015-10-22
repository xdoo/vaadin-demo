package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.ui.components.forms.BuergerPartnerForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

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

        setId(String.format("%s_%s_%s_PARENT_TAB", navigateToForDetail, from, BuergerI18nResolver.I18N_BASE_PATH));
        setCompositionRoot(vlayout);
    }
}

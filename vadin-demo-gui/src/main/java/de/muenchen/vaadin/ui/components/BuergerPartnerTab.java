package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.ui.components.forms.BuergerPartnerForm;

/**
 * @author Maximilian Schug
 */
public class BuergerPartnerTab extends CustomComponent {

    public BuergerPartnerTab(String navigateToForDetail, String navigateToForCreate) {
        BuergerPartnerForm component = new BuergerPartnerForm(navigateToForCreate, navigateToForDetail);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(component);
        vlayout.setSpacing(true);
        vlayout.setMargin(true);

        setId(String.format("%s_%s_PARENT_TAB", navigateToForDetail, BaseUI.getCurrentI18nResolver().getBasePath(Buerger.class)));
        setCompositionRoot(vlayout);
    }
}

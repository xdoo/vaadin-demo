package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.components.forms.BuergerPartnerForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 * @author Maximilian Schug
 */
public class BuergerPartnerTab extends CustomComponent {

    private BuergerPartnerForm component;

    public BuergerPartnerTab(BuergerViewController controller, String navigateToForDetail, String navigateToForCreate, String navigateToForAdd) {
        component = controller.getViewFactory().generateBuergerPartnerComponent(navigateToForCreate, navigateToForDetail, navigateToForAdd);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(component);
        vlayout.setSpacing(true);
        vlayout.setMargin(true);

        setCompositionRoot(vlayout);
    }
}

package de.muenchen.vaadin.ui.components.buttons.node.listener;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;

/**
 * Created by p.mueller on 08.10.15.
 */
public class NavigateAction implements Button.ClickListener {
    private final Navigator navigator;
    private final String navigateTo;

    public NavigateAction(Navigator navigator, String navigateTo) {
        this.navigator = navigator;
        this.navigateTo = navigateTo;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        navigator.navigateTo(navigateTo);
    }
}

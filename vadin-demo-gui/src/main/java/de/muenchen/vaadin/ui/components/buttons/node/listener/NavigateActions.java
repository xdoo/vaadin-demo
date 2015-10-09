package de.muenchen.vaadin.ui.components.buttons.node.listener;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;

/**
 * Created by p.mueller on 08.10.15.
 */
public class NavigateActions {
    private final Navigator navigator;
    private final String navigateTo;

    public NavigateActions(Navigator navigator, String navigateTo) {
        this.navigator = navigator;
        this.navigateTo = navigateTo;
    }

    public void navigate(Button.ClickEvent clickEvent) {
        navigator.navigateTo(navigateTo);
    }
}

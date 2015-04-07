/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.util;

import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import de.muenchen.vaadin.ui.app.views.MainView;

/**
 *
 * @author claus.straube
 */
public class VaadinUtil {
    
    public static Button createNavigationButton(String text, final String path) {
        Button button = new Button(text);
        button.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = -2896151918118631378L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo(path);
            }
        });
        return button;
    }
    
}

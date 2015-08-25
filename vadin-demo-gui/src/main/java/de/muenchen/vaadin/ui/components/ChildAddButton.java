/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;
import static de.muenchen.vaadin.ui.util.I18nPaths.getFormPath;

/**
 *
 * @author maximilian.schug
 */
public class ChildAddButton extends CustomComponent{
    
    public ChildAddButton(final BuergerViewController controller, final String navigateTo, final String from) {
        
        String label = controller.resolveRelative(getFormPath(I18nPaths.Action.save, I18nPaths.Component.button, I18nPaths.Type.label));
        Button create = new Button(label, FontAwesome.MAGIC);
        create.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        create.addClickListener(e -> {
            controller.getEventbus().post(new BuergerAppEvent(EventType.ADD_SEARCHED_CHILD).navigateTo(navigateTo).from(from));
        });
        create.setId(String.format("%s_%s_%s_ADD_BUTTON", navigateTo, from, BuergerViewController.I18N_BASE_PATH).toUpperCase());
        setCompositionRoot(create);
    }
      
}

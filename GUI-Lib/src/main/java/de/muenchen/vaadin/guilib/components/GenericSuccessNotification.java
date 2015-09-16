package de.muenchen.vaadin.guilib.components;

import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author claus.straube
 */
public class GenericSuccessNotification extends GenericNotification {

    public GenericSuccessNotification(String caption, String description) {
        super(caption, description, ValoTheme.NOTIFICATION_SUCCESS);
    }
    
    
    
}

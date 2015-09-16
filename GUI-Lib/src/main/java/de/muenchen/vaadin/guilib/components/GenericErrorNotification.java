package de.muenchen.vaadin.guilib.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author claus.straube
 */
public class GenericErrorNotification extends GenericNotification {

    public GenericErrorNotification(String caption, String description) {
        super(caption, description, ValoTheme.NOTIFICATION_ERROR, FontAwesome.EXCLAMATION_TRIANGLE);
    }
    
}

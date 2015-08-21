package de.muenchen.vaadin.ui.components;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author claus.straube
 */
public class GenericSuccessNotification extends Notification {

    public GenericSuccessNotification(String caption, String description) {
        super(caption, description);
        super.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
        super.setStyleName(ValoTheme.NOTIFICATION_CLOSABLE);
        super.setStyleName(ValoTheme.NOTIFICATION_TRAY);
        super.setStyleName(ValoTheme.NOTIFICATION_SMALL);
        super.setStyleName(ValoTheme.NOTIFICATION_FAILURE);
        super.setDelayMsec(3000);
        super.setPosition(Position.BOTTOM_RIGHT);
    }
    
    
    
}

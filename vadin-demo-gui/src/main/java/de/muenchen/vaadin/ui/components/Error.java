package de.muenchen.vaadin.ui.components;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author claus.straube
 */
public class Error extends Notification {

    public Error(String caption, String description) {
        super(caption, description);
        super.setStyleName(ValoTheme.NOTIFICATION_ERROR);
        super.setStyleName(ValoTheme.NOTIFICATION_CLOSABLE);
        super.setStyleName(ValoTheme.NOTIFICATION_TRAY);
        super.setStyleName(ValoTheme.NOTIFICATION_SMALL);
        super.setDelayMsec(3000);
        super.setPosition(Position.BOTTOM_RIGHT);
    }
    
}

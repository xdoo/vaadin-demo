/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.ui.components;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author claus.straube
 */
public class Success extends Notification {

    public Success(String caption, String description) {
        super(caption, description);
        super.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
        super.setStyleName(ValoTheme.NOTIFICATION_CLOSABLE);
        super.setStyleName(ValoTheme.NOTIFICATION_TRAY);
        super.setStyleName(ValoTheme.NOTIFICATION_SMALL);
        super.setDelayMsec(3000);
        super.setPosition(Position.BOTTOM_RIGHT);
    }
    
    
    
}

package de.muenchen.vaadin.guilib.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by fabian.holtkoetter on 16.09.15.
 */
public class GenericWarningNotification extends GenericNotification {

	public GenericWarningNotification(String caption, String description) {
		super(caption, description, ValoTheme.NOTIFICATION_WARNING, FontAwesome.EXCLAMATION);
	}
}

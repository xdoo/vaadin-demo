package de.muenchen.vaadin.guilib.components;

import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by fabian.holtkoetter on 16.09.15.
 */
public class GenericFailureNotification extends GenericNotification {

	public GenericFailureNotification(String caption, String description) {
		super(caption, description, ValoTheme.NOTIFICATION_FAILURE);
	}
}

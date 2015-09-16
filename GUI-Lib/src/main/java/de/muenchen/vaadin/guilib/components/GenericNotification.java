package de.muenchen.vaadin.guilib.components;

import com.vaadin.server.Resource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

/**
 * Created by fabian.holtkoetter on 16.09.15.
 */
public class GenericNotification extends Notification {

	protected GenericNotification(String caption, String description, String style) {
		this(caption, description, style, null);
	}

	protected GenericNotification(String caption, String description, String style, Resource icon) {
		super(caption, description);
		super.setStyleName(style);
		super.setIcon(icon);
		super.setDelayMsec(3000);
		super.setPosition(Position.BOTTOM_RIGHT);
	}

}

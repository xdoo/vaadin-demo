package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Window;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus
 */
@UIScope
public class ChildSelectWindow extends Window {
    

    protected static final Logger LOG = LoggerFactory.getLogger(ChildSelectWindow.class);


    public ChildSelectWindow(BuergerViewController controller, String from) {
        //TODO I18N
        super("Add Child", controller.getViewFactory().generateChildSearchTable(from));

        LOG.debug("creating 'child select window'");

        center();
        setModal(true);
        setDraggable(false);
        setResizable(false);
        
    }

    
}
package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Window;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus
 */
@UIScope
public class TableSelectWindow extends Window {


    protected static final Logger LOG = LoggerFactory.getLogger(TableSelectWindow.class);


    public TableSelectWindow(BuergerViewController controller,CustomComponent table) {

        super(controller.resolveRelative("form.add.headline.label"), table);

        center();
        setModal(true);
        setDraggable(false);
        setResizable(false);

    }


}
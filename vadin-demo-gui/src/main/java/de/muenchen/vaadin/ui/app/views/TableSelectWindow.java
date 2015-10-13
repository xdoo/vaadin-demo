package de.muenchen.vaadin.ui.app.views;

import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Window;
import de.muenchen.vaadin.services.BuergerI18nResolver;
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


    public TableSelectWindow(BuergerViewController controller, BuergerI18nResolver resolver, AbstractComponent table) {

        super(resolver.resolveRelative("form.add.headline.label"), table);

        center();
        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE);
        setDraggable(false);
        setResizable(false);
    }


}
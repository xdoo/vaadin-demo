package de.muenchen.vaadin.ui.app.views;

import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Window;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.BaseUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author claus
 */
@UIScope
public class TableSelectWindow extends Window {


    protected static final Logger LOG = LoggerFactory.getLogger(TableSelectWindow.class);


    public TableSelectWindow(AbstractComponent table) {

        super(BaseUI.getCurrentI18nResolver().resolveRelative(Buerger.class, "form.add.headline.label"), table);

        center();
        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE);
        setDraggable(false);
        setResizable(false);
    }


}
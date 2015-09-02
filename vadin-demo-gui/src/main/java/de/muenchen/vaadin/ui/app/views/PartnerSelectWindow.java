package de.muenchen.vaadin.ui.app.views;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Window;
import de.muenchen.vaadin.ui.components.BuergerPartnerSearchTable;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus
 */
@UIScope
public class PartnerSelectWindow extends Window {


    protected static final Logger LOG = LoggerFactory.getLogger(ChildSelectWindow.class);


    public PartnerSelectWindow(BuergerViewController controller, String from) {

        super(controller.resolveRelative("form.add.headline.label"), controller.getViewFactory().generateBuergerPartnerSearchTable(from));

        LOG.debug("creating 'partner select window'");

        center();
        setModal(true);
        setDraggable(false);
        setResizable(false);
        addFocusListener(focusEvent -> {
           BuergerPartnerSearchTable table =(BuergerPartnerSearchTable) super.getContent();
            table.refresh();
        });
    }


}
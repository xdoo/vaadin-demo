package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus.straube
 */
public class ChildSearchTable extends CustomComponent {
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerSearchTable.class);
    private BuergerTable table;
    public ChildSearchTable(final BuergerViewController controller, String navigateToForEdit, String navigateToForBack, String navigateToForCreate, String from, final TableActionButton.Builder... buttonfactory) {
    
        table = controller.getViewFactory().generateTable(from, buttonfactory);
        BuergerSearchForm search = new BuergerSearchForm(controller);
        search.setWidth("100%"); 
        ActionButton backButton = new ActionButton(controller, SimpleAction.back,navigateToForBack);
        backButton.addClickListener((clickEvent ->
            controller.postToEventBus(new BuergerAppEvent(EventType.CANCEL).navigateTo(navigateToForBack))
        ));
        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout(backButton, search);
        hlayout.setSpacing(true);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);
        
        setCompositionRoot(vlayout);
    }
    public BuergerTable getTable(){
        return table;
    }
    public void setTable(BuergerTable table){
        this.table = table;
    }
}
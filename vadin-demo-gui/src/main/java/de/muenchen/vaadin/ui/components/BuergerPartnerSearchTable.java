package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author max.schug
 */
public class BuergerPartnerSearchTable extends CustomComponent {
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerSearchTable.class);
    private GenericTable table;
    private BuergerSearchForm search;
    public BuergerPartnerSearchTable(final BuergerViewController controller,String from, final TableActionButton.Builder... buttonfactory) {

        table = controller.getViewFactory().generateTable(from, buttonfactory);
        search = new BuergerSearchForm(controller);
        search.setWidth("100%");
        /*ActionButton backButton = new ActionButton(controller, SimpleAction.back,navigateToForBack);
        backButton.addClickListener((clickEvent ->
                controller.postToEventBus(new BuergerAppEvent(EventType.CANCEL).navigateTo(navigateToForBack))
        ));*/
        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout( search);
        hlayout.setSpacing(true);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);

        setCompositionRoot(vlayout);
    }
    public void refresh(){
        search.refresh();
    }

}
package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.demo.i18nservice.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus.straube
 */
public class BuergerSearchTable extends CustomComponent {
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerSearchTable.class);
    private BuergerSearchForm search;
    private GenericTable table;

    public BuergerSearchTable(final BuergerViewController controller, String navigateToForCreate, final TableActionButton.Builder... buttonBuilders) {
        ActionButton create = new ActionButton(controller, SimpleAction.create,navigateToForCreate);
        create.addClickListener(clickEvent -> {
            controller.postEvent(controller.buildAppEvent(EventType.CREATE));
            controller.getNavigator().navigateTo(navigateToForCreate);
        });

        table = controller.getViewFactory().generateTable(buttonBuilders);
        search = new BuergerSearchForm(controller);
        search.setWidth("100%");
        
        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout(create, search);
        hlayout.setSpacing(true);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);
        
        setCompositionRoot(vlayout);
    }
    public GenericTable getTable(){
        return table;
    }

    public void refresh(){
        search.refresh();
    }

}

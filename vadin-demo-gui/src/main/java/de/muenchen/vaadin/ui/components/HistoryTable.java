package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

/**
 * Created by arne.schoentag on 02.09.15.
 */
public class HistoryTable extends GenericTable<Buerger> {

    //protected static final Logger LOG = LoggerFactory.getLogger(BuergerReadForm.class);

    //final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
    private GenericTable table;
    private String back;

    public HistoryTable(BuergerViewController controller,String back, final TableActionButton.Builder... buttonfactory) {
        super(controller, Buerger.class, buttonfactory);
        this.back=back;
        table = controller.getViewFactory().generateTable(back, buttonfactory);
        table.setSizeUndefined();



        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setSpacing(true);

        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);


        setCompositionRoot(vlayout);
    }

    /**
     * Eventhandler für Eventbus
     *
     * @param event the event
     */
    @Subscribe
    public void update(BuergerComponentEvent event) {

        if (event.getEventType().equals(EventType.HISTORY)) {
            LOG.error(event.getEntities().toString());
            this.addAll(event.getEntities());
        }

    }
}

package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.TableActionButton;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * Created by arne.schoentag on 02.09.15.
 */
public class HistoryTable extends GenericTable<Buerger>  implements Consumer<Event<ComponentEvent<Buerger>>> {

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
     * @param eventWrapper the event
     */
    @Override
    public void accept(reactor.bus.Event<ComponentEvent<Buerger>> eventWrapper) {
        ComponentEvent event = eventWrapper.getData();

        if (event.getEventType().equals(EventType.HISTORY)) {
            LOG.debug(event.getEntities().toString());
            this.addAll(event.getEntities());
        }

    }
}

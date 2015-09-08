package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.ComponentEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Optional;

import static de.muenchen.vaadin.ui.components.BuergerReadForm.LOG;

/**
 *
 * @author Maximilian Schug
 */
public class BuergerPartnerTab extends CustomComponent implements Consumer<Event<ComponentEvent<Buerger>>> {

    BuergerViewController controller;
    private PartnerTable table;
    public BuergerPartnerTab(BuergerViewController controller, String navigateToForDetail, String navigateToForCreate, String navigateToForAdd, String from) {

        this.controller = controller;



        ActionButton create = new ActionButton(controller, SimpleAction.create,navigateToForCreate);
        create.addClickListener(clickEvent -> {
            controller.postEvent(controller.buildAppEvent(EventType.CREATE));
            controller.getNavigator().navigateTo(navigateToForCreate);
        });
        ActionButton add = new ActionButton(controller, SimpleAction.add,navigateToForAdd);
        add.addClickListener(clickEvent -> {
            controller.postEvent(controller.buildAppEvent(EventType.ADD_PARTNER));
        });

        table = controller.getViewFactory().generatePartnerTable(navigateToForDetail);

        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout(create, add);
        hlayout.setSpacing(true);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);
        vlayout.setMargin(true);

        setId(String.format("%s_%s_%s_PARENT_TAB", navigateToForDetail, from, BuergerViewController.I18N_BASE_PATH));
        setCompositionRoot(vlayout);
    }


    public PartnerTable getTable(){
        return table;
    }
    public void setTable(PartnerTable table){
        this.table=table;
    }

    @Override
    public void accept(reactor.bus.Event<ComponentEvent<Buerger>> eventWrapper) {
        ComponentEvent event = eventWrapper.getData();

        if (event.getEventType().equals(EventType.SELECT2READ)) {
            LOG.debug("seleted buerger to show partner.");
            Optional<BeanItem<Buerger>> opt = event.getItem();
            if (opt.isPresent()) {
                Buerger entity = opt.get().getBean();
                this.controller.postEvent(controller.buildAppEvent(EventType.QUERY_CHILD).setEntity(entity));
            } else {
                LOG.warn("No item present.");
            }
        }
    }
}

package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import java.util.Optional;

import static de.muenchen.vaadin.ui.components.BuergerReadForm.LOG;

/**
 *
 * @author Maximilian Schug
 */
public class BuergerPartnerTab extends CustomComponent {

    BuergerViewController controller;
    private BuergerTable table;
    public BuergerPartnerTab(BuergerViewController controller, String navigateToForDetail, String navigateToForCreate, String navigateToForAdd, String from) {

        this.controller = controller;



        ActionButton create = new ActionButton(controller, SimpleAction.create,navigateToForCreate);
        create.addClickListener(clickEvent ->
                        controller.postToEventBus(new BuergerAppEvent(EventType.CREATE).navigateTo(navigateToForCreate).from(from))
        );
        ActionButton add = new ActionButton(controller, SimpleAction.add,navigateToForAdd);
        add.addClickListener(clickEvent ->
                        controller.postToEventBus(new BuergerAppEvent(EventType.ADD_PARTNER).navigateTo(navigateToForAdd).from(from))
        );

        table = controller.getViewFactory().generatePartnerTable(navigateToForDetail, from);

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

    @Subscribe
    public void update(BuergerComponentEvent event) {
        if (event.getEventType().equals(EventType.SELECT2READ)) {
            LOG.debug("seleted buerger to show childs.");
            Optional<BeanItem<Buerger>> opt = event.getItem();
            if (opt.isPresent()) {
                Buerger entity = opt.get().getBean();
                this.controller.getEventbus().post(new BuergerAppEvent(EventType.QUERY_CHILD).setEntity(entity));
            } else {
                LOG.warn("No item present.");
            }
        }
    }
    public BuergerTable getTable(){
        return table;
    }
    public void setTable(BuergerTable table){
        this.table=table;
    }
}

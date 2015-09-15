package de.muenchen.vaadin.ui.components;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Optional;

import static de.muenchen.vaadin.ui.components.BuergerReadForm.LOG;

/**
 *
 * @author claus
 */
public class BuergerChildTab extends CustomComponent implements Consumer<Event<ComponentEvent<Buerger>>> {

    BuergerViewController controller;
    private GenericTable table;

    public BuergerChildTab(BuergerViewController controller, String navigateToForDetail, String navigateToForCreate, String navigateBack) {
        
        this.controller = controller;



        ActionButton create = new ActionButton(controller, SimpleAction.create,navigateToForCreate);
        create.addClickListener(clickEvent -> {
            controller.postEvent(controller.buildAppEvent(EventType.CREATE));
            controller.getNavigator().navigateTo(navigateToForCreate);
        });
        ActionButton add = new ActionButton(controller, SimpleAction.add,"");
        add.addClickListener(clickEvent ->
                        controller.postEvent(controller.buildAppEvent(EventType.ADD_SEARCHED_CHILD))
        );

        table = controller.getViewFactory().generateChildTable(navigateToForDetail);
        
        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout(create, add);
        hlayout.setSpacing(true);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);
        vlayout.setMargin(true);

        setId(String.format("%s_%s_%s_CHILD_TAB", navigateToForDetail, navigateBack, BuergerViewController.I18N_BASE_PATH));
        setCompositionRoot(vlayout);
    } 

    public GenericTable getTable(){
        return table;
    }
    public void setTable(GenericTable table){
        this.table=table;
    }

    @Override
    public void accept(reactor.bus.Event<ComponentEvent<Buerger>> componentEventWrapper) {
        ComponentEvent event = componentEventWrapper.getData();
        if (event.getEventType().equals(EventType.SELECT2READ)) {
            LOG.debug("seleted buerger to show childs.");
            Optional<BeanItem<LocalBuerger>> opt = event.getItem();
            if (opt.isPresent()) {
                LocalBuerger entity = opt.get().getBean();
                this.controller.postEvent(controller.buildAppEvent(EventType.QUERY_CHILD).setEntity(entity));
            } else {
                LOG.warn("No item present.");
            }
        }
    }
}

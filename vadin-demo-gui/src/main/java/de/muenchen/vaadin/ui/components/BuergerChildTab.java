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
 * @author claus
 */
public class BuergerChildTab extends CustomComponent {

    BuergerViewController controller;
    private GenericTable table;
    public BuergerChildTab(BuergerViewController controller, String navigateToForDetail, String navigateToForCreate, String from) {
        
        this.controller = controller;



        ActionButton create = new ActionButton(controller, SimpleAction.create,navigateToForCreate);
        create.addClickListener(clickEvent ->
            controller.postToEventBus(new BuergerAppEvent(EventType.CREATE).navigateTo(navigateToForCreate).from(from))
        );
        ActionButton add = new ActionButton(controller, SimpleAction.add,"");
        add.addClickListener(clickEvent ->
            controller.postToEventBus(new BuergerAppEvent(EventType.ADD_SEARCHED_CHILD).from(from))
        );

        table = controller.getViewFactory().generateChildTable(navigateToForDetail, from);
        
        // Layout für die Schaltflächen über der Tabelle
        HorizontalLayout hlayout = new HorizontalLayout(create, add);
        hlayout.setSpacing(true);
        // Gesamtlayout
        VerticalLayout vlayout = new VerticalLayout(hlayout, table);
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        setId(String.format("%s_%s_%s_CHILD_TAB", navigateToForDetail, from, BuergerViewController.I18N_BASE_PATH));
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
    public GenericTable getTable(){
        return table;
    }
    public void setTable(GenericTable table){
        this.table=table;
    }
}

package de.muenchen.vaadin.ui.controller.factorys;

import com.vaadin.data.util.BeanItem;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.events.RefreshEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.ui.components.*;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.bus.Event;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static reactor.bus.selector.Selectors.T;

/**
 * Created by rene.zarwel on 26.08.15.
 */
@Component
@UIScope
public class BuergerViewFactory implements Serializable, Consumer<Event<RefreshEvent>> {

    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerViewFactory.class);
    private static final long serialVersionUID = 1L;
    @Autowired
    EventBus eventBus;
    private BuergerViewController controller;

    /**Singeltons of Components. **/
    private transient Optional<GenericGrid> childSearchTable = Optional.empty();
    private transient Optional<BuergerChildTab> childTab = Optional.empty();
    private transient Optional<BuergerCreateForm> createForm = Optional.empty();
    private transient Optional<BuergerCreateForm> createChildForm = Optional.empty();
    private transient Optional<BuergerCreateForm> createPartnerForm = Optional.empty();
    private transient Optional<BuergerUpdateForm> updateForm = Optional.empty();
    private transient Optional<BuergerReadForm> readForm = Optional.empty();
    private transient Optional<GenericGrid> partnerSearchTable = Optional.empty();
    private transient Optional<BuergerPartnerTab> partnerTab = Optional.empty();

    @PostConstruct
    public void init(){
        eventBus.on(T(RefreshEvent.class), this);
    }


    //////////////////////////////////////////////
    // Factory Methoden für die UI Komponenten //
    //////////////////////////////////////////////

    public BuergerCreateForm generateCreateForm(String navigateTo) {
        LOG.debug("creating 'create' buerger form");
        if(!createForm.isPresent()){
            BuergerCreateForm form = new BuergerCreateForm(controller, navigateTo, EventType.SAVE);
            createForm = Optional.of(form);}
        return createForm.get();
    }

    public BuergerCreateForm generateCreateChildForm(String navigateTo) {
        LOG.debug("creating 'create child' buerger form");
        if(!createChildForm.isPresent()){
            BuergerCreateForm form = new BuergerCreateForm(controller, navigateTo, EventType.SAVE_CHILD);
            createChildForm=Optional.of(form);}
        return createChildForm.get();
    }

    public BuergerCreateForm generateCreatePartnerForm(String navigateTo) {
        LOG.debug("creating 'create partner' buerger form");
        if (!createPartnerForm.isPresent()) {
            BuergerCreateForm form = new BuergerCreateForm(controller, navigateTo, EventType.SAVE_PARTNER);
            createPartnerForm = Optional.of(form);
        }
        return createPartnerForm.get();
    }

    /**
     * Erzeugt eine neue Instanz eines "Child" Tabs.
     *
     * @param navigateToForDetail Zielseite um sich die Details des 'Child' Objektes anzeigen zu lassen
     * @param navigateForCreate Zielseite um ein neues 'Child' Objekt zu erstellen
     * @param navigateBack Ausgangsseite zu der zurück navigiert werden soll
     * @return {@link TabSheet.Tab} das Tab
     */
    public BuergerChildTab generateChildTab(String navigateToForDetail, String navigateForCreate, String navigateBack) {
        if(!childTab.isPresent())
        {
            BuergerChildTab tab = new BuergerChildTab(controller, navigateToForDetail, navigateForCreate, navigateBack);
            controller.registerToAllComponentEvents(tab);
            childTab = Optional.of(tab);}
        return childTab.get();
    }

    public BuergerPartnerTab generatePartnerTab(String navigateToForDetail, String navigateForCreate, String navigateForAdd, String navigateBack) {
        if(!partnerTab.isPresent())
        {
            BuergerPartnerTab tab = new BuergerPartnerTab(controller, navigateToForDetail, navigateForCreate, navigateForAdd, navigateBack);
            controller.registerToAllComponentEvents(tab);
            partnerTab = Optional.of(tab);}
        return partnerTab.get();
    }

    public BuergerUpdateForm generateUpdateForm(String navigateTo, String navigateBack) {
        LOG.debug("creating 'update' buerger form");
        if(!updateForm.isPresent()){
            BuergerUpdateForm form = new BuergerUpdateForm(controller, navigateTo, navigateBack);
            controller.registerToAllComponentEvents(form);
            controller.postEvent(controller.buildComponentEvent(EventType.SELECT2UPDATE).addEntity(controller.getCurrent().getBean()));

            updateForm=Optional.of(form);}
        return updateForm.get();
    }

    public BuergerReadForm generateReadForm(String navigateToUpdate, String navigateBack) {
        LOG.debug("creating 'read' buerger form");
        if(!readForm.isPresent()){
            BuergerReadForm form = new BuergerReadForm(controller, navigateToUpdate, navigateBack);
            controller.registerToAllComponentEvents(form);
            readForm=Optional.of(form);}
        controller.postEvent(controller.buildComponentEvent(EventType.SELECT2READ).addEntity(controller.getCurrent().getBean()));
        return readForm.get();
    }

    public GenericGrid generateChildSearchTable() {
        if (!childSearchTable.isPresent()) {
            LOG.debug("creating 'search' table for buerger");
            childSearchTable = Optional.of(generateGrid());
            childSearchTable.get().removeColumn("id");
            childSearchTable.get().removeColumn("links");
            childSearchTable.get().addItemClickListener(itemClickEvent -> {
                if (itemClickEvent.isDoubleClick()) {
                    controller.postEvent(controller.buildAppEvent(EventType.SAVE_AS_CHILD).setItem((BeanItem<LocalBuerger>) itemClickEvent.getItem()));
                }
            });
        }
            return childSearchTable.get();
    }

    public GenericGrid generateBuergerPartnerSearchTable() {
        if (!partnerSearchTable.isPresent()) {
            LOG.debug("creating 'search' table for buerger");
            partnerSearchTable = Optional.of(generateGrid());
            partnerSearchTable.get().removeColumn("id");
            partnerSearchTable.get().removeColumn("links");
            partnerSearchTable.get().addItemClickListener(itemClickEvent -> {
                if (itemClickEvent.isDoubleClick()) {
                    controller.postEvent(controller.buildAppEvent(EventType.SAVE_AS_PARTNER).setItem((BeanItem<LocalBuerger>) itemClickEvent.getItem()));
                }
            });
        }
        return partnerSearchTable.get();
    }

    public KindGrid generateChildTable(String navigateToForDetail) {
        LOG.debug("creating table for children");
        KindGrid table = new KindGrid(controller);
        table.setSizeFull();
        table.setSelectionMode(Grid.SelectionMode.MULTI);
        table.removeColumn("id");
        table.removeColumn("links");
        table.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                if (itemClickEvent.isDoubleClick()) {
                    controller.postEvent(controller.buildAppEvent(EventType.SELECT2READ).setItem((BeanItem<LocalBuerger>) itemClickEvent.getItem()));
                    controller.getNavigator().navigateTo(navigateToForDetail);
                    return;
                }
                boolean isClicked = table.isSelected(itemClickEvent.getItemId());
                if (table.getSelectedRows().size() > 0 && !itemClickEvent.isCtrlKey()) {
                    table.getSelectedRows().stream().forEach(row -> table.deselect(row));
                }
                if (!isClicked)
                    table.select(itemClickEvent.getItemId());
                else
                    table.deselect(itemClickEvent.getItemId());
            }
        });

        List<LocalBuerger> entities = controller.queryKinder(controller.getCurrent().getBean());
        controller.registerToAllComponentEvents(table);
        controller.postEvent(controller.buildComponentEvent(EventType.QUERY_CHILD).addEntities(entities));

        return table;
    }

    public PartnerGrid generatePartnerTable(String navigateToForDetail) {

        LOG.debug("creating table for partner");
        PartnerGrid table = new PartnerGrid(controller);
        table.setSizeFull();
        table.setSelectionMode(Grid.SelectionMode.MULTI);
        table.removeColumn("id");
        table.removeColumn("links");
        table.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                if (itemClickEvent.isDoubleClick()) {
                    controller.postEvent(controller.buildAppEvent(EventType.SELECT2READ).setItem((BeanItem<LocalBuerger>) itemClickEvent.getItem()));
                    controller.getNavigator().navigateTo(navigateToForDetail);
                    return;
                }
                boolean isClicked = table.isSelected(itemClickEvent.getItemId());
                if (!itemClickEvent.isCtrlKey()) {
                    table.getSelectedRows().stream().forEach(row -> table.deselect(row));
                }
                if (!isClicked)
                    table.select(itemClickEvent.getItemId());
                else
                    table.deselect(itemClickEvent.getItemId());
            }
        });

        List<LocalBuerger> entities = controller.queryPartner(controller.getCurrent().getBean());
        controller.registerToAllComponentEvents(table);
        controller.postEvent(controller.buildComponentEvent(EventType.QUERY_PARTNER).addEntities(entities));

        return table;
    }

    public GenericGrid generateGrid() {
        return this.createGrid();
    }

    private GenericGrid createGrid() {
        LOG.debug("creating table for buerger");
        GenericGrid grid = new GenericGrid(controller,LocalBuerger.class);
        controller.registerToAllComponentEvents(grid);
        return grid;
    }

    public BuergerGrid generateBuergerGrid() {
        LOG.debug("creating buergerGrid");
        BuergerGrid buergerGrid = new BuergerGrid(controller);
        return buergerGrid;
    }

    public BuergerViewController getController() {
        return controller;
    }

    public void setController(BuergerViewController controller) {
        this.controller = controller;
    }

    @Override
    public void accept(reactor.bus.Event<RefreshEvent> eventWrapper){
        LOG.debug("RefreshEvent received");
        childSearchTable = Optional.empty();
        childTab = Optional.empty();
        createForm = Optional.empty();
        createChildForm = Optional.empty();
        createPartnerForm = Optional.empty();
        updateForm = Optional.empty();
        readForm = Optional.empty();
        partnerSearchTable = Optional.empty();
        partnerTab = Optional.empty();
        controller.getNavigator().navigateTo(controller.getNavigator().getState());
    }
}

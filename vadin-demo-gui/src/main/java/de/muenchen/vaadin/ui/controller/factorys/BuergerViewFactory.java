package de.muenchen.vaadin.ui.controller.factorys;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.Util.Association;
import de.muenchen.eventbus.oldEvents.RefreshEvent;
import de.muenchen.eventbus.types.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.ui.components.GenericGrid;
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
import java.util.Optional;

import static reactor.bus.selector.Selectors.$;
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

    /** Singeltons of Components. **/
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
    public void init() {
        eventBus.on(T(RefreshEvent.class), this);
    }


    //////////////////////////////////////////////
    // Factory Methoden für die UI Komponenten //
    //////////////////////////////////////////////

    public BuergerCreateForm generateCreateForm(String navigateTo) {
        LOG.debug("creating 'create' buerger form");
        if (!createForm.isPresent()) {
            BuergerCreateForm form = new BuergerCreateForm(controller, navigateTo, null);
            createForm = Optional.of(form);
        }
        return createForm.get();
    }

    public BuergerCreateForm generateCreateChildForm(String navigateTo) {
        LOG.debug("creating 'create child' buerger form");
        if (!createChildForm.isPresent()) {
            BuergerCreateForm form = new BuergerCreateForm(controller, navigateTo, Buerger.Rel.kinder.name());
            createChildForm = Optional.of(form);
        }
        return createChildForm.get();
    }

    public BuergerCreateForm generateCreatePartnerForm(String navigateTo) {
        LOG.debug("creating 'create partner' buerger form");
        if (!createPartnerForm.isPresent()) {
            BuergerCreateForm form = new BuergerCreateForm(controller, navigateTo, Buerger.Rel.partner.name());
            createPartnerForm = Optional.of(form);
        }
        return createPartnerForm.get();
    }

    /**
     * Erzeugt eine neue Instanz eines "Child" Tabs.
     *
     * @param navigateToForDetail Zielseite um sich die Details des 'Child' Objektes anzeigen zu lassen
     * @param navigateForCreate   Zielseite um ein neues 'Child' Objekt zu erstellen
     * @param navigateBack        Ausgangsseite zu der zurück navigiert werden soll
     * @return {@link TabSheet.Tab} das Tab
     */
    public BuergerChildTab generateChildTab(String navigateToForDetail, String navigateForCreate, String navigateBack) {
        if (!childTab.isPresent()) {
            BuergerChildTab tab = new BuergerChildTab(controller, navigateToForDetail, navigateForCreate, navigateBack);
            childTab = Optional.of(tab);
        }
        return childTab.get();
    }

    public BuergerPartnerTab generatePartnerTab(String navigateToForDetail, String navigateForCreate, String navigateForAdd, String navigateBack) {
        if (!partnerTab.isPresent()) {
            BuergerPartnerTab tab = new BuergerPartnerTab(controller, navigateToForDetail, navigateForCreate, navigateForAdd, navigateBack);
            partnerTab = Optional.of(tab);
        }
        return partnerTab.get();
    }

    public BuergerUpdateForm generateUpdateForm(String navigateTo, String navigateBack) {
        LOG.debug("creating 'update' buerger form");
        if (!updateForm.isPresent()) {
            BuergerUpdateForm form = new BuergerUpdateForm(controller, navigateTo, navigateBack);
            controller.getEventbus().on($(controller.getResponseKey()), form);
            updateForm = Optional.of(form);
        }
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return updateForm.get();
    }

    public BuergerReadForm generateReadForm(String navigateToUpdate, String navigateBack) {
        LOG.debug("creating 'read' buerger form");
        if (!readForm.isPresent()) {
            BuergerReadForm form = new BuergerReadForm(controller, navigateToUpdate, navigateBack);
            controller.getEventbus().on($(controller.getResponseKey()), form);
            readForm = Optional.of(form);
        }
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
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
                    Association<Buerger> association = new Association<>((Buerger) itemClickEvent.getItemId(), Buerger.Rel.kinder.name());
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.UPDATE), Event.wrap(association));
                }
            });
        }
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
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
                    Association<Buerger> association = new Association<>((Buerger) itemClickEvent.getItemId(), Buerger.Rel.partner.name());
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.UPDATE), Event.wrap(association));
                }
            });
        }
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return partnerSearchTable.get();
    }

    public KindGrid generateChildTable(String navigateToForDetail) {
        LOG.debug("creating table for children");
        KindGrid grid = new KindGrid(controller);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.removeColumn("id");
        grid.removeColumn("links");
        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                if (itemClickEvent.isDoubleClick()) {
                    //TODO controller.postEvent(controller.buildAppEvent(EventType.SELECT_TO_READ).setItem((BeanItem<Buerger>) itemClickEvent.getItem()));
                    controller.getNavigator().navigateTo(navigateToForDetail);
                    return;
                }
                boolean isClicked = grid.isSelected(itemClickEvent.getItemId());
                if (grid.getSelectedRows().size() > 0 && !itemClickEvent.isCtrlKey()) {
                    grid.getSelectedRows().stream().forEach(row -> grid.deselect(row));
                }
                if (!isClicked)
                    grid.select(itemClickEvent.getItemId());
                else
                    grid.deselect(itemClickEvent.getItemId());
            }
        });

        controller.getEventbus().on($(controller.getResponseKey()), grid);
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));

        return grid;
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
                    //TODO controller.postEvent(controller.buildAppEvent(EventType.SELECT_TO_READ).setItem((BeanItem<Buerger>) itemClickEvent.getItem()));
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

        controller.getEventbus().on($(controller.getResponseKey()), table);
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));

        return table;
    }

    public GenericGrid generateGrid() {
        return this.createGrid();
    }

    private GenericGrid createGrid() {
        LOG.debug("creating table for buerger");
        GenericGrid grid = new GenericGrid(controller, Buerger.class);
        controller.getEventbus().on($(controller.getResponseKey()), grid);
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
    public void accept(reactor.bus.Event<RefreshEvent> eventWrapper) {
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

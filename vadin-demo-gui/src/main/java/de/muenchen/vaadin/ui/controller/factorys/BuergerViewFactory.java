package de.muenchen.vaadin.ui.controller.factorys;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.Key;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.services.BuergerI18nResolver;
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

/**
 * Created by rene.zarwel on 26.08.15.
 */
@Component
@UIScope
public class BuergerViewFactory implements Serializable, Consumer<Event<?>> {

    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerViewFactory.class);
    private static final long serialVersionUID = 1L;
    @Autowired
    EventBus eventBus;
    @Autowired
    BuergerI18nResolver resolver;
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
        eventBus.on(Key.REFRESH.toSelector(), this);
    }


    //////////////////////////////////////////////
    // Factory Methoden für die UI Komponenten //
    //////////////////////////////////////////////

    public BuergerCreateForm generateCreateForm(String navigateTo) {
        LOG.debug("creating 'create' buerger form");
        if (!createForm.isPresent()) {
            BuergerCreateForm form = new BuergerCreateForm(controller, resolver, navigateTo, null);
            createForm = Optional.of(form);
        }
        return createForm.get();
    }

    public BuergerCreateForm generateCreateChildForm(String navigateTo) {
        LOG.debug("creating 'create child' buerger form");
        if (!createChildForm.isPresent()) {
            BuergerCreateForm form = new BuergerCreateForm(controller, resolver, navigateTo, Buerger.Rel.kinder.name());
            createChildForm = Optional.of(form);
        }
        return createChildForm.get();
    }

    public BuergerCreateForm generateCreatePartnerForm(String navigateTo) {
        LOG.debug("creating 'create partner' buerger form");
        if (!createPartnerForm.isPresent()) {
            BuergerCreateForm form = new BuergerCreateForm(controller, resolver, navigateTo, Buerger.Rel.partner.name());
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
            BuergerChildTab tab = new BuergerChildTab(controller, resolver, navigateToForDetail, navigateForCreate, navigateBack);
            childTab = Optional.of(tab);
        }
        return childTab.get();
    }

    public BuergerPartnerTab generatePartnerTab(String navigateToForDetail, String navigateForCreate, String navigateForAdd, String navigateBack) {
        if (!partnerTab.isPresent()) {
            BuergerPartnerTab tab = new BuergerPartnerTab(controller, resolver, navigateToForDetail, navigateForCreate, navigateForAdd, navigateBack);
            partnerTab = Optional.of(tab);
        }
        return partnerTab.get();
    }

    public BuergerUpdateForm generateUpdateForm(String navigateTo, String navigateBack) {
        LOG.debug("creating 'update' buerger form");
        if (!updateForm.isPresent()) {
            BuergerUpdateForm form = new BuergerUpdateForm(controller, resolver, navigateTo, navigateBack);
            controller.getEventbus().on(controller.getResponseKey().toSelector(), form);
            updateForm = Optional.of(form);
        }
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return updateForm.get();
    }

    public BuergerReadForm generateReadForm(String navigateToUpdate, String navigateBack) {
        LOG.debug("creating 'read' buerger form");
        if (!readForm.isPresent()) {
            BuergerReadForm form = new BuergerReadForm(controller, resolver, navigateToUpdate, navigateBack);
            controller.getEventbus().on(controller.getResponseKey().toSelector(), form);
            readForm = Optional.of(form);
        }
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return readForm.get();
    }

    public GenericGrid generateChildSearchTable() {
        if (!childSearchTable.isPresent()) {
            LOG.debug("creating 'search' table for buerger");
            childSearchTable = Optional.of(generateGrid());
            childSearchTable.get().addItemClickListener(itemClickEvent -> {
                if (itemClickEvent.isDoubleClick()) {
                    Association<Buerger> association = new Association<>((Buerger) itemClickEvent.getItemId(), Buerger.Rel.kinder.name());
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.ADD_ASSOCIATION), association.asEvent());
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
            partnerSearchTable.get().addItemClickListener(itemClickEvent -> {
                if (itemClickEvent.isDoubleClick()) {
                    Association<Buerger> association = new Association<>((Buerger) itemClickEvent.getItemId(), Buerger.Rel.partner.name());
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.ADD_ASSOCIATION), association.asEvent());
                }
            });
        }
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return partnerSearchTable.get();
    }

    public KindGrid generateChildTable(String navigateToForDetail) {
        LOG.debug("creating table for children");
        KindGrid grid = new KindGrid(resolver);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                if (itemClickEvent.isDoubleClick()) {
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED), Event.wrap((itemClickEvent.getItemId())));
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

        controller.getEventbus().on(controller.getResponseKey().toSelector(), grid);
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));

        return grid;
    }

    public BuergerPartnerComponent generateBuergerPartnerComponent(String navigateToForCreate){
        BuergerPartnerComponent partnerComponent = new BuergerPartnerComponent(controller, navigateToForCreate);

        controller.getEventbus().on(controller.getResponseKey().toSelector(), partnerComponent);
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return partnerComponent;
    }

    public GenericGrid generateGrid() {
        return this.createGrid();
    }

    private GenericGrid createGrid() {
        LOG.debug("creating table for buerger");
        GenericGrid grid = new GenericGrid(resolver, Buerger.class);
        grid.setColumns(Buerger.Field.getProperties());
        controller.getEventbus().on(controller.getResponseKey().toSelector(), grid);
        return grid;
    }

    public BuergerGrid generateBuergerGrid() {
        LOG.debug("creating buergerGrid");
        BuergerGrid buergerGrid = new BuergerGrid(controller, resolver);
        return buergerGrid;
    }

    public BuergerViewController getController() {
        return controller;
    }

    public void setController(BuergerViewController controller) {
        this.controller = controller;
    }

    @Override
    public void accept(reactor.bus.Event<?> event) {
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

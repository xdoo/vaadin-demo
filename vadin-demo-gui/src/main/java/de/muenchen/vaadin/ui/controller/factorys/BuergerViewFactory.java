package de.muenchen.vaadin.ui.controller.factorys;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.ui.components.*;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.bus.Event;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * Created by rene.zarwel on 26.08.15.
 */
@Component
@UIScope
public class BuergerViewFactory implements Serializable {

    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerViewFactory.class);
    private static final long serialVersionUID = 1L;
    @Autowired
    EventBus eventBus;
    private BuergerViewController controller;

    @PostConstruct
    public void init() {
        // TODO REMOVE eventBus.on(Key.REFRESH.toSelector(), this);
    }


    //////////////////////////////////////////////
    // Factory Methoden für die UI Komponenten //
    //////////////////////////////////////////////

    public BuergerCreateForm generateCreateForm(String navigateTo) {
        return new BuergerCreateForm(controller, navigateTo, null);
    }

    public BuergerCreateForm generateCreateChildForm(String navigateTo) {
        return new BuergerCreateForm(controller, navigateTo, Buerger.Rel.kinder.name());
    }

    public BuergerCreateForm generateCreatePartnerForm(String navigateTo) {
        return new BuergerCreateForm(controller, navigateTo, Buerger.Rel.partner.name());
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
        return new BuergerChildTab(controller, navigateToForDetail, navigateForCreate, navigateBack);
    }

    public BuergerPartnerTab generatePartnerTab(String navigateToForDetail, String navigateForCreate, String navigateForAdd, String navigateBack) {
        return new BuergerPartnerTab(controller, navigateToForDetail, navigateForCreate, navigateForAdd, navigateBack);
    }

    public BuergerUpdateForm generateUpdateForm(String navigateTo, String navigateBack) {
        BuergerUpdateForm form = new BuergerUpdateForm(controller, navigateTo, navigateBack);
        controller.getEventbus().on(controller.getResponseKey().toSelector(), form);

        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return form;
    }

    public BuergerReadForm generateReadForm(String navigateToUpdate, String navigateBack) {
        BuergerReadForm form = new BuergerReadForm(controller, navigateToUpdate, navigateBack);
        controller.getEventbus().on(controller.getResponseKey().toSelector(), form);

        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return form;
    }

    public GenericGrid generateChildSearchTable() {
        final GenericGrid components = generateGrid();
        components.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.isDoubleClick()) {
                Association<Buerger> association = new Association<>((Buerger) itemClickEvent.getItemId(), Buerger.Rel.kinder.name());
                controller.getEventbus().notify(controller.getRequestKey(RequestEvent.ADD_ASSOCIATION), association.asEvent());
            }
        });
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return components;
    }

    public GenericGrid generateBuergerPartnerSearchTable() {
        final GenericGrid components = generateGrid();
        components.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.isDoubleClick()) {
                Association<Buerger> association = new Association<>((Buerger) itemClickEvent.getItemId(), Buerger.Rel.partner.name());
                controller.getEventbus().notify(controller.getRequestKey(RequestEvent.ADD_ASSOCIATION), association.asEvent());
            }
        });
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return components;
    }

    public KindGrid generateChildTable(String navigateToForDetail) {
        LOG.debug("creating table for children");
        KindGrid grid = new KindGrid(controller);
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

    public PartnerGrid generatePartnerTable(String navigateToForDetail) {

        LOG.debug("creating table for partner");
        PartnerGrid table = new PartnerGrid(controller);
        table.setSizeFull();
        table.setSelectionMode(Grid.SelectionMode.MULTI);
        table.addItemClickListener(itemClickEvent -> {
            if (itemClickEvent.getPropertyId() != null) {
                if (itemClickEvent.isDoubleClick()) {
                    controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED), Event.wrap((itemClickEvent.getItemId())));
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

        controller.getEventbus().on(controller.getResponseKey().toSelector(), table);
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));

        return table;
    }

    public GenericGrid generateGrid() {
        return this.createGrid();
    }

    private GenericGrid createGrid() {
        LOG.debug("creating table for buerger");
        GenericGrid grid = new GenericGrid(controller, Buerger.class);
        grid.setColumns(Buerger.Field.getProperties());
        controller.getEventbus().on(controller.getResponseKey().toSelector(), grid);
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
}

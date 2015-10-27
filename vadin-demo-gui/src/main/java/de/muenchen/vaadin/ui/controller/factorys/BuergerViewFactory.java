package de.muenchen.vaadin.ui.controller.factorys;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nResolverImpl;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.ui.components.BuergerChildTab;
import de.muenchen.vaadin.ui.components.BuergerGrid;
import de.muenchen.vaadin.ui.components.BuergerPartnerTab;
import de.muenchen.vaadin.ui.components.KindGrid;
import de.muenchen.vaadin.ui.components.forms.BuergerCreateForm;
import de.muenchen.vaadin.ui.components.forms.BuergerPartnerForm;
import de.muenchen.vaadin.ui.components.forms.BuergerRWForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    I18nResolverImpl resolver;
    private BuergerViewController controller;


    //////////////////////////////////////////////
    // Factory Methoden für die UI Komponenten //
    //////////////////////////////////////////////

    public BuergerCreateForm generateCreateForm(String navigateTo, String navigateBack) {
        return new BuergerCreateForm(navigateTo);
    }

    public BuergerCreateForm generateCreateChildForm(String navigateTo, String navigateBack) {
        return new BuergerCreateForm(navigateTo, Buerger.Rel.kinder.name());
    }

    public BuergerCreateForm generateCreatePartnerForm(String navigateTo, String navigateBack) {
        return new BuergerCreateForm(navigateTo, Buerger.Rel.partner.name());
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
        return new BuergerChildTab(controller, controller.getResolver(), navigateToForDetail, navigateForCreate, navigateBack);
    }

    public BuergerPartnerTab generatePartnerTab(String navigateToForDetail, String navigateForCreate, String navigateBack) {
        return new BuergerPartnerTab(controller, navigateToForDetail, navigateForCreate, navigateBack);
    }

    public BuergerRWForm generateRWForm(String navigateBack) {
        BuergerRWForm form = new BuergerRWForm();
        getEventBus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return form;
    }

    public EventBus getEventBus() {
        return BaseUI.getCurrentEventBus();
    }

    public GenericGrid<Buerger> generateBuergerSearchTable() {
        final GenericGrid components = generateGrid();
        getEventBus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return components;
    }

    public GenericGrid<Buerger> generateGrid() {
        return new GenericGrid<>(controller.getModel().getBuergers(), Buerger.Field.getProperties());
    }

    public KindGrid generateChildTable(String navigateToForDetail) {
        LOG.debug("creating table for children");
        KindGrid grid = new KindGrid(controller);

        grid.activateDoubleClickToRead(navigateToForDetail);

        getEventBus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));

        return grid;
    }

    public BuergerPartnerForm generateBuergerPartnerComponent(String navigateToForCreate, String navigateToRead) {
        BuergerPartnerForm partnerComponent = new BuergerPartnerForm(navigateToForCreate, navigateToRead);
        getEventBus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return partnerComponent;
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

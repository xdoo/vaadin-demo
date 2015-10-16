package de.muenchen.vaadin.ui.controller.factorys;

import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TabSheet;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.guilib.components.GenericGrid;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.ui.components.BuergerChildTab;
import de.muenchen.vaadin.ui.components.BuergerGrid;
import de.muenchen.vaadin.ui.components.BuergerPartnerComponent;
import de.muenchen.vaadin.ui.components.BuergerPartnerTab;
import de.muenchen.vaadin.ui.components.KindGrid;
import de.muenchen.vaadin.ui.components.forms.BuergerCreateForm;
import de.muenchen.vaadin.ui.components.forms.SelectedBuergerReadForm;
import de.muenchen.vaadin.ui.components.forms.SelectedBuergerUpdateForm;
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
    EventBus eventBus;
    @Autowired
    BuergerI18nResolver resolver;
    private BuergerViewController controller;


    //////////////////////////////////////////////
    // Factory Methoden für die UI Komponenten //
    //////////////////////////////////////////////

    public BuergerCreateForm generateCreateForm(String navigateTo, String navigateBack) {
        return new BuergerCreateForm(controller, navigateTo, navigateBack, null);
    }

    public BuergerCreateForm generateCreateChildForm(String navigateTo, String navigateBack) {
        return new BuergerCreateForm(controller, navigateTo, navigateBack, Buerger.Rel.kinder.name());
    }

    public BuergerCreateForm generateCreatePartnerForm(String navigateTo, String navigateBack) {
        return new BuergerCreateForm(controller, navigateTo, navigateBack, Buerger.Rel.partner.name());
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

    public SelectedBuergerUpdateForm generateUpdateForm(String navigateTo, String navigateBack) {
        SelectedBuergerUpdateForm form = new SelectedBuergerUpdateForm(controller, navigateTo, navigateBack);
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return form;
    }

    public SelectedBuergerReadForm generateReadForm(String navigateToUpdate, String navigateBack) {
        SelectedBuergerReadForm form = new SelectedBuergerReadForm(controller, navigateToUpdate, navigateBack);
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return form;
    }


    public GenericGrid generateChildSearchTable() {
        final GenericGrid components = generateGrid();
        components.addDoubleClickListener(buerger -> {
            Association<Buerger> association = new Association<>((Buerger) buerger, Buerger.Rel.kinder.name());
            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.ADD_ASSOCIATION), association.asEvent());
        });
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return components;
    }


    public GenericGrid generateBuergerPartnerSearchTable() {
        final GenericGrid components = generateGrid();
        components.addDoubleClickListener(buerger -> {
            Association<Buerger> association = new Association<>((Buerger) buerger, Buerger.Rel.partner.name());
            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.ADD_ASSOCIATION), association.asEvent());
        });
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return components;
    }

    public KindGrid generateChildTable(String navigateToForDetail) {
        LOG.debug("creating table for children");
        KindGrid grid = new KindGrid(controller);

        grid.activateDoubleClickToRead(navigateToForDetail);

        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));

        return grid;
    }

    public BuergerPartnerComponent generateBuergerPartnerComponent(String navigateToForCreate) {
        BuergerPartnerComponent partnerComponent = new BuergerPartnerComponent(controller, resolver, navigateToForCreate);

        controller.getEventbus().on(controller.getResponseKey().toSelector(), partnerComponent);
        controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED));
        return partnerComponent;
    }

    public BuergerGrid generateBuergerGrid() {
        LOG.debug("creating buergerGrid");
        BuergerGrid buergerGrid = new BuergerGrid(controller);
        return buergerGrid;
    }

    public GenericGrid<Buerger> generateGrid() {
        return new GenericGrid<>(controller, controller.getModel().getBuergers(), Buerger.Field.getProperties());
    }

    public BuergerViewController getController() {
        return controller;
    }

    public void setController(BuergerViewController controller) {
        this.controller = controller;
    }
}

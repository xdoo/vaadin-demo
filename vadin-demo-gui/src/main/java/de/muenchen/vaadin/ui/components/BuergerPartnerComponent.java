package de.muenchen.vaadin.ui.components;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericConfirmationWindow;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * Created by claus.straube on 05.10.15.
 * fabian.holtkoetter ist unschuldig.
 */
public class BuergerPartnerComponent extends CustomComponent implements Consumer<Event<BuergerDatastore>> {

    /**
     * UI Elements
     */
    private BuergerViewController controller;
    private BuergerI18nResolver resolver;
    private FormLayout partnerReadForm;
    private ActionButton create;
    private ActionButton add;
    private ActionButton read;
    private ActionButton delete;

    private Buerger currentPartner;
    /**
     * Navigation Strings
     */
    private final String navigateToForCreate;

    public BuergerPartnerComponent(BuergerViewController controller, BuergerI18nResolver resolver, String navigateToForCreate) {
        this.controller = controller;
        this.resolver = resolver;
        this.navigateToForCreate = navigateToForCreate;
        this.buildComponent();
    }

    private void buildComponent() {
        create = buildCreateButton();
        add = buildAddButton();
        read = buildReadButton();
        delete = buildDeleteButton();
        partnerReadForm = new FormLayout();
        partnerReadForm.setStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        partnerReadForm.setWidthUndefined();


        // Layout für ActionButtons
        HorizontalLayout buttonLayout = new HorizontalLayout(create, add, read, delete);
        buttonLayout.setSpacing(true);
        // Komponente

        VerticalLayout vlayout = new VerticalLayout(buttonLayout, partnerReadForm);
        vlayout.setSpacing(true);
        vlayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        setId(String.format("%s_PARENT_COMPONENT", BuergerI18nResolver.I18N_BASE_PATH));
        setCompositionRoot(vlayout);
    }

    private ActionButton buildReadButton() {
        ActionButton read = new ActionButton(resolver, SimpleAction.read, navigateToForCreate);
        read.addClickListener(clickEvent -> {
            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.READ_SELECTED), reactor.bus.Event.wrap(currentPartner));
            controller.getNavigator().navigateTo(BuergerDetailView.NAME);
        });
        read.setVisible(false);
        return read;
    }


    private ActionButton buildCreateButton() {
        ActionButton create = new ActionButton(resolver, SimpleAction.create, navigateToForCreate);
        create.addClickListener(clickEvent -> {
            if (partnerReadForm.getComponentCount() == 0) {
                controller.getNavigator().navigateTo(navigateToForCreate);
            } else {
                GenericConfirmationWindow window = new GenericConfirmationWindow(resolver, SimpleAction.override, e -> {
                    controller.getNavigator().navigateTo(navigateToForCreate);
                });
                getUI().addWindow(window);
                window.center();
                window.focus();
            }
        });
        return create;
    }

    private ActionButton buildAddButton() {
        ActionButton add = new ActionButton(resolver, SimpleAction.add, null);
        add.addClickListener(clickEvent -> {
            if (partnerReadForm.getComponentCount() == 0) {
                HorizontalLayout layout = new HorizontalLayout(controller.getViewFactory().generateBuergerPartnerSearchTable());
                layout.setMargin(true);
                getUI().addWindow(new TableSelectWindow(controller, resolver, layout));
            } else {
                GenericConfirmationWindow window =
                        new GenericConfirmationWindow(resolver, SimpleAction.override, e ->
                        {
                            HorizontalLayout layout = new HorizontalLayout(controller.getViewFactory().generateBuergerPartnerSearchTable());
                            layout.setMargin(true);
                            getUI().addWindow(new TableSelectWindow(controller, resolver, layout));
                        });
                getUI().addWindow(window);
                window.center();
                window.focus();
            }
        });
        return add;
    }

    private ActionButton buildDeleteButton() {
        ActionButton delete = new ActionButton(resolver, SimpleAction.delete, null);
        delete.addClickListener(event -> {
            Association<Buerger> association = new Association<>(new Buerger(), Buerger.Rel.partner.name());
            controller.getEventbus().notify(controller.getRequestKey(RequestEvent.REMOVE_ASSOCIATION), association.asEvent());
        });
        delete.setVisible(false);
        return delete;
    }

    @Override
    public void accept(reactor.bus.Event<BuergerDatastore> buergerDatastoreEvent) {
        BuergerDatastore datastore = buergerDatastoreEvent.getData();
        if (datastore.getSelectedBuergerPartner().size() > 0) {
            currentPartner = datastore.getSelectedBuergerPartner().getItemIds().get(0);
            partnerReadForm.removeAllComponents();
            BeanItem<Buerger> partner = buergerDatastoreEvent.getData().getSelectedBuergerPartner().getItem(currentPartner);

            FieldGroup binder = new FieldGroup(partner);
            binder.setReadOnly(true);
            partnerReadForm.addComponent(binder.buildAndBind(Buerger.Field.vorname.name()));
            partnerReadForm.addComponent(binder.buildAndBind(Buerger.Field.nachname.name()));
            partnerReadForm.addComponent(binder.buildAndBind(Buerger.Field.geburtsdatum.name()));
            read.setVisible(true);
            delete.setVisible(true);
        } else {
            currentPartner = null;
            partnerReadForm.removeAllComponents();
            read.setVisible(false);
            delete.setVisible(false);
        }
    }
}

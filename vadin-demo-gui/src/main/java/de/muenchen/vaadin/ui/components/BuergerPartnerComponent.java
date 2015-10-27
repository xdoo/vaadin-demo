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
import de.muenchen.vaadin.demo.i18nservice.I18nResolverImpl;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericConfirmationWindow;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.app.views.BuergerDetailView;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Optional;

/**
 * Created by claus.straube on 05.10.15. fabian.holtkoetter ist unschuldig.
 */
public class BuergerPartnerComponent extends CustomComponent implements Consumer<Event<BuergerDatastore>> {

    /**
     * Navigation Strings
     */
    private final String navigateToForCreate;
    /**
     * UI Elements
     */
    private BuergerViewController controller;
    private I18nResolverImpl resolver;
    private FormLayout partnerReadForm;
    private ActionButton create;
    private ActionButton add;
    private ActionButton read;
    private ActionButton delete;
    private Buerger currentPartner;

    public BuergerPartnerComponent(BuergerViewController controller, I18nResolverImpl resolver, String navigateToForCreate) {
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
        setId(String.format("%s_PARENT_COMPONENT", resolver.getBasePath(Buerger.class)));
        setCompositionRoot(vlayout);
    }

    private ActionButton buildReadButton() {
        ActionButton read = new ActionButton(Buerger.class, SimpleAction.read);
        final NavigateActions navigateAction = new NavigateActions(BuergerDetailView.NAME);
        final BuergerSingleActions buergerSingleActions = new BuergerSingleActions(() -> currentPartner);

        read.addActionPerformer(buergerSingleActions::read);
        read.addActionPerformer(navigateAction::navigate);

        read.setVisible(false);
        return read;
    }


    private ActionButton buildCreateButton() {
        ActionButton create = new ActionButton(Buerger.class, SimpleAction.create);
        create.addClickListener(clickEvent -> {
            NavigateActions navigateAction = new NavigateActions(navigateToForCreate);
            if (partnerReadForm.getComponentCount() == 0) {
                navigateAction.navigate();
            } else {
                GenericConfirmationWindow window = new GenericConfirmationWindow(resolver, SimpleAction.override, e -> {
                    navigateAction.navigate();
                });
                getUI().addWindow(window);
                window.center();
                window.focus();
            }
        });
        return create;
    }

    private ActionButton buildAddButton() {
        ActionButton add = new ActionButton(Buerger.class, SimpleAction.add);
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
        ActionButton delete = new ActionButton(Buerger.class, SimpleAction.delete);
        delete.addClickListener(event -> {
            Association<Buerger> association = new Association<>(new Buerger(), Buerger.Rel.partner.name());
            BaseUI.getCurrentEventBus().notify(controller.getRequestKey(RequestEvent.REMOVE_ASSOCIATION), association.asEvent());
        });
        delete.setVisible(false);
        return delete;
    }

    @Override
    public void accept(reactor.bus.Event<BuergerDatastore> buergerDatastoreEvent) {
        BuergerDatastore datastore = buergerDatastoreEvent.getData();
        if (!datastore.getSelectedBuergerPartner().equals(Optional.empty())) {
            currentPartner = datastore.getSelectedBuergerPartner().get();
            partnerReadForm.removeAllComponents();
            BeanItem<Buerger> partner = new BeanItem(buergerDatastoreEvent.getData().getSelectedBuergerPartner().get());

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

package de.muenchen.vaadin.ui.components;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericConfirmationWindow;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.app.views.TableSelectWindow;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.apache.commons.lang.WordUtils;
import reactor.bus.Event;
import reactor.fn.Consumer;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;

/**
 * Created by claus.straube on 05.10.15.
 * fabian.holtkoetter ist unschuldig.
 */
public class BuergerPartnerComponent extends CustomComponent implements Consumer<Event<BuergerDatastore>> {

    /**
     * UI Elements
     */
    private BuergerViewController controller;
    private FormLayout partnerReadForm;
    private ActionButton create;
    private ActionButton add;
    private ActionButton delete;

    /**
     * Navigation Strings
     */
    private final String navigateToForCreate;

    public BuergerPartnerComponent(BuergerViewController controller, String navigateToForCreate) {
        this.controller = controller;

        this.navigateToForCreate = navigateToForCreate;

        this.buildComponent();
    }

    private void buildComponent() {
        create = buildCreateButton();
        add = buildAddButton();
        delete = buildDeleteButton();
        partnerReadForm = new FormLayout();
        partnerReadForm.setStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        partnerReadForm.setWidthUndefined();


        // Layout für ActionButtons
        HorizontalLayout buttonLayout = new HorizontalLayout(create, add, delete);
        buttonLayout.setSpacing(true);
        // Komponente

        VerticalLayout vlayout = new VerticalLayout(buttonLayout, partnerReadForm);
        vlayout.setSpacing(true);
        vlayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        setId(String.format("%s_PARENT_COMPONENT", BuergerViewController.I18N_BASE_PATH));
        setCompositionRoot(vlayout);
    }


    private ActionButton buildCreateButton() {
        ActionButton create = new ActionButton(controller, SimpleAction.create, navigateToForCreate);
        create.addClickListener(clickEvent -> {
            if (partnerReadForm.getComponentCount() == 0) {
                controller.getNavigator().navigateTo(navigateToForCreate);
            } else {
                GenericConfirmationWindow window = new GenericConfirmationWindow(controller, SimpleAction.override, e -> {
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
        ActionButton add = new ActionButton(controller, SimpleAction.add, null);
        add.addClickListener(clickEvent -> {
            if (partnerReadForm.getComponentCount() == 0) {
                Window w = new TableSelectWindow(controller, controller.getViewFactory().generateBuergerPartnerSearchTable());
//                w.getContent().setSizeUndefined();
                w.getContent().setSizeFull();
                getUI().addWindow(w);
            } else {
                GenericConfirmationWindow window =
                        new GenericConfirmationWindow(controller, SimpleAction.override, e -> getUI().addWindow(new TableSelectWindow(controller, controller.getViewFactory().generateBuergerPartnerSearchTable())));
                getUI().addWindow(window);
                window.center();
                window.focus();
            }
        });
        return add;
    }

    private ActionButton buildDeleteButton() {
        ActionButton delete = new ActionButton(controller, SimpleAction.delete, null);
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
            partnerReadForm.removeAllComponents();
            Buerger itemID = datastore.getSelectedBuergerPartner().getItemIds().get(0);
            BeanItem<Buerger> partner = buergerDatastoreEvent.getData().getSelectedBuergerPartner().getItem(itemID);

            FieldGroup binder = new FieldGroup(partner);
            binder.setReadOnly(true);
            partnerReadForm.addComponent(binder.buildAndBind(Buerger.Field.vorname.name()));
            partnerReadForm.addComponent(binder.buildAndBind(Buerger.Field.nachname.name()));
            partnerReadForm.addComponent(binder.buildAndBind(Buerger.Field.geburtsdatum.name()));
            delete.setVisible(true);
        } else {
            partnerReadForm.removeAllComponents();
            delete.setVisible(false);
        }
    }
}

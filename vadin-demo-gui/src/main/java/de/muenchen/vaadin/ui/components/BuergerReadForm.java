package de.muenchen.vaadin.ui.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.events.ComponentEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.api.domain.Augenfarbe;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Optional;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.*;

/**
 *
 * @author claus
 */
public class BuergerReadForm extends CustomComponent implements Consumer<Event<ComponentEvent<Buerger>>> {
    
    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerReadForm.class);

    final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
    final BuergerViewController controller;
    
    private final String navigateToUpdate;
    private final String navigateBack;


    /**
     * Formular zum Lesen eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'zurück' und 'bearbeiten' Schaltflächen
     * erstellt werden. 
     * 
     * @param controller
     * @param navigateToUpdate
     * @param navigateBack
     */
    public BuergerReadForm(BuergerViewController controller, final String navigateToUpdate, String navigateBack) {
        
        this.controller = controller;
        this.navigateToUpdate = navigateToUpdate;
        this.navigateBack = navigateBack;

        // create form
        this.createForm();

    }
    
    /**
     * Erzeugt das eigentliche Formular.
     */
    private void createForm() {
        
        FormLayout layout = new FormLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        layout.setMargin(true);
        
        // headline
        Label headline = new Label(controller.resolveRelative(getFormPath(SimpleAction.read, I18nPaths.Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        layout.addComponent(controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.input_prompt)),
                Buerger.Field.vorname.name(), BuergerViewController.I18N_BASE_PATH));
        layout.addComponent(controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.input_prompt)),
                Buerger.Field.nachname.name(), BuergerViewController.I18N_BASE_PATH));
        layout.addComponent(controller.getUtil().createFormComboBox(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.augenfarbe.name(), Type.label)),
                Buerger.Field.augenfarbe.name(),
                Augenfarbe.class));
        layout.addComponent(controller.getUtil().createFormDateField(
                binder, controller.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), Type.label)),
                Buerger.Field.geburtsdatum.name(), BuergerViewController.I18N_BASE_PATH));
        
        // auf 'read only setzen
        this.binder.setReadOnly(true);
        layout.addComponent(buttonLayout);
        // die Schaltfläche zum Aktualisieren
        ActionButton backButton = new ActionButton(controller, SimpleAction.back, this.navigateBack);
        backButton.addClickListener(clickEvent -> controller.getNavigator().navigateTo(getNavigateBack()));
        buttonLayout.addComponent(backButton);

        // die Schaltfläche zum Bearbeiten
        ActionButton updateButton = new ActionButton(controller, SimpleAction.update,this.navigateToUpdate);
        updateButton.addClickListener(clickEvent -> {
            controller.postEvent(controller.buildAppEvent(EventType.SELECT2UPDATE).setEntity(this.binder.getItemDataSource().getBean()).setItem(binder.getItemDataSource()));
            controller.getNavigator().navigateTo(navigateToUpdate);
        });
        buttonLayout.addComponent(updateButton);
        setCompositionRoot(layout);
    }

    @Override
    public void accept(reactor.bus.Event<ComponentEvent<Buerger>> eventWrapper) {
        ComponentEvent event = eventWrapper.getData();

        if (event.getEventType().equals(EventType.SELECT2READ)) {
            LOG.debug("seleted buerger to modify.");
            Optional<BeanItem<Buerger>> opt = event.getItem();
            if (opt.isPresent()) {
                this.binder.setItemDataSource(opt.get());
            } else {
                LOG.warn("No item present.");
            }
        }
    }

    public String getNavigateBack() {
        return navigateBack;
    }
}

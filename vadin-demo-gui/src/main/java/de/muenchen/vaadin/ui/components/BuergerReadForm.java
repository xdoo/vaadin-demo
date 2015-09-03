package de.muenchen.vaadin.ui.components;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.I18nPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Optional;

import static de.muenchen.vaadin.ui.util.I18nPaths.Type;
import static de.muenchen.vaadin.ui.util.I18nPaths.getEntityFieldPath;
import static de.muenchen.vaadin.ui.util.I18nPaths.getFormPath;

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
    private String back;
    private final String from;


    /**
     * Formular zum Lesen eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'zurück' und 'bearbeiten' Schaltflächen
     * erstellt werden. 
     * 
     * @param controller
     * @param navigateToUpdate
     * @param back 
     * @param from 
     */
    public BuergerReadForm(BuergerViewController controller, final String navigateToUpdate, String back, final String from) {
        
        this.controller = controller;
        this.navigateToUpdate = navigateToUpdate;
        this.back = back;
        this.from = from;
        
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
                controller.resolveRelative(getEntityFieldPath(Buerger.VORNAME, Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.VORNAME, Type.input_prompt)),
                Buerger.VORNAME, BuergerViewController.I18N_BASE_PATH));
        layout.addComponent(controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.input_prompt)),
                Buerger.NACHNAME, BuergerViewController.I18N_BASE_PATH));
/*
        layout.addComponent(controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.input_prompt)),
                Buerger.NACHNAME, BuergerViewController.I18N_BASE_PATH));
        */
        layout.addComponent(controller.getUtil().createFormDateField(
                binder, controller.resolveRelative(getEntityFieldPath(Buerger.GEBURTSDATUM, Type.label)),
                Buerger.GEBURTSDATUM, BuergerViewController.I18N_BASE_PATH));
       
        // auf 'read only setzen
        this.binder.setReadOnly(true);
        layout.addComponent(buttonLayout);
        // die Schaltfläche zum Aktualisieren
        ActionButton backButton = new ActionButton(controller, SimpleAction.back,this.back);
        backButton.addClickListener((clickEvent -> {
            controller.postEvent(new AppEvent<Buerger>(EventType.CANCEL).navigateTo(this.back));
        }));
        buttonLayout.addComponent(backButton);

        // die Schaltfläche zum Bearbeiten
        ActionButton updateButton = new ActionButton(controller, SimpleAction.update,this.navigateToUpdate);
        updateButton.addClickListener(clickEvent -> {
            controller.postEvent(new AppEvent<Buerger>(EventType.SELECT2UPDATE).setEntity(this.binder.getItemDataSource().getBean()).setItem(binder.getItemDataSource()).navigateTo(this.navigateToUpdate).from(this.from));
        });
        buttonLayout.addComponent(updateButton);
        setCompositionRoot(layout);
    }

    public String getNavigateBack() {
        return back;
    }

    public void setNavigateBack(String back) {
        this.back = back;
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
}

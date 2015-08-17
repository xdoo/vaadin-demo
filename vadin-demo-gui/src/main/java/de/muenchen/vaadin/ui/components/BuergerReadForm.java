package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import static de.muenchen.vaadin.ui.util.I18nPaths.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus
 */
public class BuergerReadForm extends CustomComponent {
    
    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerReadForm.class);
    
    final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
    private BuergerUpdateButton updateButton;
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
        Label headline = new Label(controller.resolve(getFormPath(Action.read, Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        layout.addComponent(controller.getUtil().createFormTextField(binder,
                controller.resolve(getEntityFieldPath(Buerger.VORNAME, Type.label)),
                controller.resolve(getEntityFieldPath(Buerger.VORNAME, Type.input_prompt)),
                Buerger.VORNAME));
        layout.addComponent(controller.getUtil().createFormTextField(binder,
                controller.resolve(getEntityFieldPath(Buerger.NACHNAME, Type.label)),
                controller.resolve(getEntityFieldPath(Buerger.NACHNAME, Type.input_prompt)),
                Buerger.NACHNAME));
        layout.addComponent(controller.getUtil().createFormDateField(
                binder, controller.resolve(getEntityFieldPath(Buerger.GEBURTSDATUM, Type.label)),
                Buerger.GEBURTSDATUM));
        
        // auf 'read only setzen
        this.binder.setReadOnly(true);
        layout.addComponent(buttonLayout);
        // die Schaltfläche zum Aktualisieren        
        buttonLayout.addComponent(new BuergerBackButton(this.controller, this.back));
        // die Schaltfläche zum Bearbeiten
        this.updateButton = new BuergerUpdateButton(this.controller, this.navigateToUpdate, this.from);
        buttonLayout.addComponent(this.updateButton);
        setCompositionRoot(layout);
    }
    
    @Subscribe
    public void update(BuergerComponentEvent event) {     
        if (event.getEventType().equals(EventType.SELECT2READ)) {
            LOG.debug("seleted buerger to modify.");
            Optional<BeanItem<Buerger>> opt = event.getItem();
            if (opt.isPresent()) {
                this.binder.setItemDataSource(opt.get());
                this.updateButton.setEntity(opt.get().getBean());
            } else {
                LOG.warn("No item present.");
            }
        }
    }

    public String getNavigateBack() {
        return back;
    }

    public void setNavigateBack(String back) {
        this.back = back;
    }

}

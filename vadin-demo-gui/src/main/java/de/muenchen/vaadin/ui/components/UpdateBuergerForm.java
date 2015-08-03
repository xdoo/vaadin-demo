package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus
 */
public class UpdateBuergerForm extends CustomComponent {
    
    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(UpdateBuergerForm.class);
    
    final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
    final BuergerViewController controller;
    
    private final String navigateTo;
    private String navigateBack;

    /**
     * Formular zum Bearbeiten eines {@link Buerger}s. Über diesen
     * Konstruktor wir die Zielseite für die 'erstellen' Schaltfläche automatisch
     * zur Zielseite für die 'abbrechen' Schaltfläche. Dies ist in den meisten Fällen
     * das gewollte verhalten.
     * 
     * @param controller
     * @param navigateTo 
     */
    public UpdateBuergerForm(BuergerViewController controller, String navigateTo) {
        this.controller = controller;
        this.navigateTo = navigateTo;
        this.navigateBack = navigateTo;
        
        // create form
        this.createForm();
    }

    /**
     * Formular zum Bearbeiten eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'abbrechen' Schaltfläche erstellt werden. 
     * Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in eine
     * definierte Abfolge von Formularen eingebettet wird.
     * 
     * @param controller
     * @param navigateTo
     * @param navigateBack 
     */
    public UpdateBuergerForm(BuergerViewController controller, final String navigateTo, String navigateBack) {
        
        this.controller = controller;
        this.navigateTo = navigateTo;
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
        Label headline = new Label(controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_UPDATE_HEADLINE_LABEL));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.VORNAME, controller.getMsg()));
        layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.NACHNAME, controller.getMsg()));
        layout.addComponent(controller.getUtil().createFormDateField(binder, controller.getI18nBasePath(), Buerger.GEBURTSDATUM, controller.getMsg()));
        
        layout.addComponent(buttonLayout);
        // die Schaltfläche zum Aktualisieren
        String update = controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_UPDATE_BUTTON_LABEL);
        Button updateButton = new Button(update, (Button.ClickEvent click) -> {
            try {
                binder.commit();
                Buerger entity = binder.getItemDataSource().getBean();
                BuergerEvent event = new BuergerEvent(entity, EventType.UPDATE);
                event.setNavigateTo(navigateTo);
                controller.getEventbus().post(event);
            } catch (FieldGroup.CommitException e) {
                Notification.show("You fail!");
            }
        });
        updateButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        updateButton.setIcon(FontAwesome.PENCIL);
        buttonLayout.addComponent(updateButton);
        // die Schaltfläche zum Abbrechen
        buttonLayout.addComponent(new GenericCancelButton(
                controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CANCEL_BUTTON_LABEL), 
                new BuergerEvent(null, EventType.CANCEL).setNavigateTo(this.navigateBack), // hier kann eine 'null' gesetzt werden, weil nichts mehr mit dem Objekt passiert
                this.controller.getEventbus()));
        setCompositionRoot(layout);
    }
    
    @Subscribe
    public void update(BuergerComponentEvent event) {     
        if (event.getEventType().equals(EventType.SELECT2UPDATE)) {
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

    public void setNavigateBack(String navigateBack) {
        this.navigateBack = navigateBack;
    }

}

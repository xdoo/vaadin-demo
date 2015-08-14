package de.muenchen.vaadin.ui.components;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus
 */
public class BuergerUpdateForm extends CustomComponent {
    
    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerUpdateForm.class);
    
    final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
    final BuergerViewController controller;
    
    private final String navigateTo;
    private String back;
    private String from;

    /**
     * Formular zum Bearbeiten eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'abbrechen' Schaltfläche erstellt werden. 
     * Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in eine
     * definierte Abfolge von Formularen eingebettet wird.
     * 
     * @param controller
     * @param navigateTo
     * @param back 
     * @param from 
     */
    public BuergerUpdateForm(BuergerViewController controller, final String navigateTo, String back, String from) {
        
        this.controller = controller;
        this.navigateTo = navigateTo;
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
        Label headline = new Label(controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_UPDATE_HEADLINE_LABEL));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        //layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.VORNAME, controller.getMsg()));
        //layout.addComponent(controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.NACHNAME, controller.getMsg()));
        //layout.addComponent(controller.getUtil().createFormDateField(binder, controller.getI18nBasePath(), Buerger.GEBURTSDATUM, controller.getMsg()));
        
        
        TextField firstField = controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.VORNAME, controller.getMsg());
        firstField.focus();
        firstField.addValidator(new StringLengthValidator(controller.getMsg().get("m1.buerger.nachname.validation"),1,Integer.MAX_VALUE, false));
        layout.addComponent(firstField);
        
        // alle anderen Felder
        TextField secField = controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.NACHNAME, controller.getMsg());
        secField.addValidator(new StringLengthValidator(controller.getMsg().get("m1.buerger.nachname.validation"),1,Integer.MAX_VALUE, false));
        layout.addComponent(secField);
        DateField birthdayfield = controller.getUtil().createFormDateField(binder, controller.getI18nBasePath(), Buerger.GEBURTSDATUM, controller.getMsg());      
        String errorMsg = controller.getMsg().get("m1.buerger.geburtsdatum.validation");
        birthdayfield.addValidator(new DateRangeValidator(errorMsg,new Date(0),new Date(),DateField.RESOLUTION_YEAR));
        layout.addComponent(birthdayfield); 
        
        
        layout.addComponent(buttonLayout);
        // die Schaltfläche zum Aktualisieren
        String update = controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_UPDATE_BUTTON_LABEL);
        Button updateButton = new Button(update, (Button.ClickEvent click) -> {
            try {
                binder.commit();
                Buerger entity = binder.getItemDataSource().getBean();
                controller.getEventbus().post(new BuergerAppEvent(entity, EventType.UPDATE).navigateTo(navigateTo).from(this.from));
            } catch (FieldGroup.CommitException e) {
                Notification.show("You fail!");
            }
        });
        updateButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        updateButton.setIcon(FontAwesome.PENCIL);
        updateButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttonLayout.addComponent(updateButton);
        // die Schaltfläche zum Abbrechen
        buttonLayout.addComponent(new GenericCancelButton(
                controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CANCEL_BUTTON_LABEL), 
                new BuergerAppEvent(null, EventType.CANCEL).navigateTo(this.back), // hier kann eine 'null' gesetzt werden, weil nichts mehr mit dem Objekt passiert
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
        return back;
    }

    public void setNavigateBack(String navigateBack) {
        this.back = navigateBack;
    }

}

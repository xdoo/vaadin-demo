package de.muenchen.vaadin.ui.components;

import com.oracle.jrockit.jfr.InvalidValueException;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;
import de.muenchen.vaadin.ui.util.ValidatorFactory;
import java.util.Date;

/**
 * Formular zum Erstellen eines {@link Buerger}s.
 * 
 * @author claus.straube
 */
public class BuergerCreateForm extends CustomComponent {
    
    private final String navigateTo;
    private String back;
    private final BuergerViewController controller;
    private EventType type = EventType.SAVE;

    /**
     * Formular zum Erstellen eines {@link Buerger}s. Über diesen
     * Konstruktor wir die Zielseite für die 'erstellen' Schaltfläche automatisch
     * zur Zielseite für die 'abbrechen' Schaltfläche. Dies ist in den meisten Fällen
     * das gewollte verhalten.
     * 
     * @param controller der Entity Controller
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     */
    public BuergerCreateForm(final BuergerViewController controller, final String navigateTo) {
        this.navigateTo = navigateTo;
        this.back = navigateTo;
        this.controller = controller;
        
        this.createForm();
    }
    
    /**
     * Formular zum Erstellen eines {@link Buerger}s. Über diesen Konstruktor kann
     * zusätzlich eine Zielseite für die 'abbrechen' Schaltfläche erstellt werden. 
     * Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in eine
     * definierte Abfolge von Formularen eingebettet wird.
     * 
     * @param controller der Entity Controller
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     * @param back Zielseite nach Druck der 'abbrechen' Schaltfläche
     */
    public BuergerCreateForm(final BuergerViewController controller, final String navigateTo, String back) {
        this.navigateTo = navigateTo;
        this.back = back;
        this.controller = controller;
        
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
        Label headline = new Label(controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CREATE_HEADLINE_LABEL));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        // Now use a binder to bind the members
        final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
        binder.setItemDataSource(controller.createBuerger());
        
        // Fokus auf das erste Feld setzen
        TextField firstField = controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.VORNAME, controller.getMsg());
        firstField.focus();
        firstField.addValidator(new StringLengthValidator(controller.getMsg().get("m1.buerger.nachname.validation"),1,Integer.MAX_VALUE, true));
        layout.addComponent(firstField);
        
        // alle anderen Felder
        TextField secField = controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.NACHNAME, controller.getMsg());
        secField.addValidator(ValidatorFactory.getValidator("StringLength",controller.getMsg().get("m1.buerger.nachname.validation"),1+"",""+Integer.MAX_VALUE, "true"));
        layout.addComponent(secField);
        DateField birthdayfield = controller.getUtil().createFormDateField(binder, controller.getI18nBasePath(), Buerger.GEBURTSDATUM, controller.getMsg());
        //String errorMsg = controller.getUtil().createFormTextField(binder, controller.getI18nBasePath(), Buerger.GEBURTSDATUM+".validation", controller.getMsg()).getValue();
        String errorMsg = controller.getMsg().get("m1.buerger.geburtsdatum.validation");
        birthdayfield.addValidator(new DateRangeValidator(errorMsg,new Date(0),new Date(),DateField.RESOLUTION_YEAR));
        layout.addComponent(birthdayfield);    

        layout.addComponent(buttonLayout);
        // die 'speichern' Schaltfläche
        String createLabel = controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CREATE_BUTTON_LABEL);
        Button createButton = new Button(createLabel, (ClickEvent click) -> {
            try {
                Validator val = ValidatorFactory.getValidator("Null",controller.getMsg().get("m1.buerger.nachname.validation"),"false");
                firstField.addValidator(val);
                secField.addValidator(val);
                birthdayfield.addValidator(val);
                firstField.validate();                
                secField.validate();
                birthdayfield.validate();
                binder.commit();
                controller.getEventbus().post(new BuergerAppEvent(binder.getItemDataSource().getBean(), this.type).navigateTo(navigateTo));
                //reset
                binder.setItemDataSource(controller.createBuerger());
            } catch (CommitException | Validator.InvalidValueException e) {
                GenericErrorNotification error = new GenericErrorNotification("Fehler","Beim erstellen der Person ist ein Fehler aufgetreten. Bitte füllen Sie alle Felder mit gültigen Werten aus.");
                error.show(Page.getCurrent());
            }
        });
        createButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        createButton.setIcon(FontAwesome.MAGIC);
        createButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttonLayout.addComponent(createButton);
        // die 'abbrechen' Schaltfläche
        buttonLayout.addComponent(new GenericCancelButton(
                controller.getMsg().readText(controller.getI18nBasePath(), I18nPaths.I18N_FORM_CANCEL_BUTTON_LABEL), 
                new BuergerAppEvent(binder.getItemDataSource().getBean(), EventType.CANCEL).navigateTo(this.back), 
                this.controller.getEventbus()));
        setCompositionRoot(layout);
    }

    public String getNavigateTo() {
        return navigateTo;
    }

    public BuergerViewController getController() {
        return controller;
    }

    public String getNavigateBack() {
        return back;
    }

    public void setNavigateBack(String navigateBack) {
        this.back = navigateBack;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}

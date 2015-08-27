package de.muenchen.vaadin.ui.components;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
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
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.ValidatorFactory;

import static de.muenchen.vaadin.ui.util.I18nPaths.Component;
import de.muenchen.vaadin.ui.util.I18nPaths.NotificationType;
import static de.muenchen.vaadin.ui.util.I18nPaths.Type;
import static de.muenchen.vaadin.ui.util.I18nPaths.getEntityFieldPath;
import static de.muenchen.vaadin.ui.util.I18nPaths.getFormPath;
import static de.muenchen.vaadin.ui.util.I18nPaths.getNotificationPath;

/**
 * Formular zum Erstellen eines {@link Buerger}s.
 * 
 * @author claus.straube
 */
public class BuergerCreateForm extends CustomComponent {

    private final String navigateTo;
    private String back;
    private final BuergerViewController controller;
    private EventType type;

    /**
     * Formular zum Erstellen eines {@link Buerger}s. Über diesen
     * Konstruktor wir die Zielseite für die 'erstellen' Schaltfläche automatisch
     * zur Zielseite für die 'abbrechen' Schaltfläche. Dies ist in den meisten Fällen
     * das gewollte verhalten.
     * 
     * @param controller der Entity Controller
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     */
    public BuergerCreateForm(final BuergerViewController controller, final String navigateTo, EventType type) {
        this.navigateTo = navigateTo;
        this.back = navigateTo;
        this.controller = controller;
        this.type=type;
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
        Validator val = ValidatorFactory.getValidator("Null",controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.validation)),"false");
        FormLayout layout = new FormLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        layout.setMargin(true);
        
        // headline
        Label headline = new Label(controller.resolveRelative(
                getFormPath(SimpleAction.create,
                        Component.headline,
                        Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        // Now use a binder to bind the members
        final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<>(Buerger.class);
        binder.setItemDataSource(controller.createBuerger());
        
        // Fokus auf das erste Feld setzen
        TextField firstField = controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.VORNAME, Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.VORNAME, Type.input_prompt)),
                Buerger.VORNAME, BuergerViewController.I18N_BASE_PATH);
        firstField.focus();
        String abc = "";
        for(char c = 'a';c <= 'z'; c++)
            abc+=c;
        for(char c = 'A';c <= 'Z'; c++)
            abc+=c;
        for(int i =192;i<=382;i++)
            abc+=Character.toString((char)i);
        for(int i =7682;i<=7807;i++)
            abc+=Character.toString((char)i);
        abc+="-";
    
        Validator val0 = ValidatorFactory.getValidator("Regexp",controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.validationstring)),"true","["+abc+"]*");
        
        Validator val1 = ValidatorFactory.getValidator("StringLength", controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.validation)), 1 + "", "" + Integer.MAX_VALUE, "true");
        firstField.addValidator(val0);
        firstField.addValidator(val1);
        layout.addComponent(firstField);
        
        // alle anderen Felder
        TextField secField = controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.input_prompt)),
                Buerger.NACHNAME, BuergerViewController.I18N_BASE_PATH);
        secField.addValidator(val1);
        secField.addValidator(val0);
        layout.addComponent(secField);
        DateField birthdayfield = controller.getUtil().createFormDateField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.GEBURTSDATUM, Type.label)),
                Buerger.GEBURTSDATUM, BuergerViewController.I18N_BASE_PATH);
        String errorMsg = controller.resolveRelative(getEntityFieldPath(Buerger.GEBURTSDATUM, Type.validation));
        Validator val3 = ValidatorFactory.getValidator("DateRange",errorMsg,"0",null);
        birthdayfield.addValidator(val3);
        layout.addComponent(birthdayfield);    

        layout.addComponent(buttonLayout);
        // die 'speichern' Schaltfläche
        String createLabel = controller.resolveRelative(
                getFormPath(SimpleAction.create,
                        Component.button,
                        Type.label));
        Button createButton = new Button(createLabel, (ClickEvent click) -> {
            try {
                //If no NullValidators are added to the Fields, they will be added.
                if(!firstField.getValidators().contains(val)){
                    firstField.addValidator(val);
                    secField.addValidator(val);                
                    birthdayfield.addValidator(val);
                }
                //Validation of the Fields before continuing
                firstField.validate();                
                secField.validate();
                birthdayfield.validate();
                
                binder.commit();
                controller.getEventbus().post(new BuergerAppEvent(binder.getItemDataSource().getBean(), this.type).navigateTo(navigateTo));

                //reset
                firstField.removeValidator(val);
                secField.removeValidator(val);
                birthdayfield.removeValidator(val);
                binder.setItemDataSource(controller.createBuerger());
            } catch (CommitException | Validator.InvalidValueException e) {
                GenericErrorNotification error = new GenericErrorNotification(controller.resolveRelative(getNotificationPath(NotificationType.failure,SimpleAction.save, Type.label)),
                        controller.resolveRelative(getNotificationPath(NotificationType.failure,SimpleAction.save,Type.text)));
                        error.show(Page.getCurrent());
            }
        });
        createButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        createButton.setIcon(FontAwesome.MAGIC);
        createButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttonLayout.addComponent(createButton);
        // die 'abbrechen' Schaltfläche
        buttonLayout.addComponent(new GenericCancelButton(
                controller.resolveRelative(getFormPath(SimpleAction.cancel, Component.button, Type.label)),
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

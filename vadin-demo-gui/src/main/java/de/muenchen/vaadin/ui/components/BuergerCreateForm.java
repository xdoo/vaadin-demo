package de.muenchen.vaadin.ui.components;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
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
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericWarningNotification;
import de.muenchen.vaadin.guilib.util.ValidatorFactory;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Component;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.NotificationType;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;

/**
 * Formular zum Erstellen eines {@link Buerger}s.
 * 
 * @author claus.straube
 */
public class BuergerCreateForm extends CustomComponent {

    private final String navigateTo;
    private final String back;
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
        Validator val = ValidatorFactory.getValidator(ValidatorFactory.Type.NULL, controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), "false");
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
        binder.setItemDataSource(new Buerger());
        
        // Fokus auf das erste Feld setzen
        TextField firstField = controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.input_prompt)),
                Buerger.Field.vorname.name(), BuergerViewController.I18N_BASE_PATH);
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

        Validator val0 = ValidatorFactory.getValidator(ValidatorFactory.Type.REGEXP, controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validationstring)), "true", "[" + abc + "]*");

        Validator val1 = ValidatorFactory.getValidator(ValidatorFactory.Type.STRING_LENGTH, controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), 1 + "", "" + Integer.MAX_VALUE, "true");
        firstField.addValidator(val0);
        firstField.addValidator(val1);
        layout.addComponent(firstField);
        
        // alle anderen Felder
        TextField secField = controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.input_prompt)),
                Buerger.Field.nachname.name(), BuergerViewController.I18N_BASE_PATH);
        secField.addValidator(val1);
        secField.addValidator(val0);
        layout.addComponent(secField);
        DateField birthdayfield = controller.getUtil().createFormDateField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), Type.label)),
                Buerger.Field.geburtsdatum.name(), BuergerViewController.I18N_BASE_PATH);
        String errorMsg = controller.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), Type.validation));
        Validator val3 = ValidatorFactory.getValidator(ValidatorFactory.Type.DATE_RANGE, errorMsg, "start", null);
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
                controller.postEvent(controller.buildAppEvent(this.type).setEntity(binder.getItemDataSource().getBean()));
                getNavigator().navigateTo(getNavigateTo());

                //reset
                firstField.removeValidator(val);
                secField.removeValidator(val);
                birthdayfield.removeValidator(val);
                binder.setItemDataSource(new Buerger());
            } catch (CommitException | Validator.InvalidValueException e) {
                GenericWarningNotification warn = new GenericWarningNotification(
                        controller.resolveRelative(getNotificationPath(NotificationType.warning, SimpleAction.save, Type.label)),
                        controller.resolveRelative(getNotificationPath(NotificationType.warning, SimpleAction.save, Type.text)));
                warn.show(Page.getCurrent());
            }
        });
        createButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        createButton.setIcon(FontAwesome.MAGIC);
        createButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        buttonLayout.addComponent(createButton);
        // die 'abbrechen' Schaltfläche

        ActionButton back = new ActionButton(controller, SimpleAction.back, "lsadf");
        back.addClickListener(clickEvent -> getNavigator().navigateTo(getNavigateBack()));
        buttonLayout.addComponent(back);

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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    private Navigator getNavigator() {
        return getController().getNavigator();
    }
}

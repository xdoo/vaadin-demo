package de.muenchen.vaadin.ui.components;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.SimpleAction;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.util.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Optional;

import static de.muenchen.vaadin.ui.util.I18nPaths.Component;
import static de.muenchen.vaadin.ui.util.I18nPaths.Type;
import static de.muenchen.vaadin.ui.util.I18nPaths.getEntityFieldPath;
import static de.muenchen.vaadin.ui.util.I18nPaths.getFormPath;
import static reactor.bus.selector.Selectors.T;

/**
 *
 * @author claus
 */
public class BuergerUpdateForm extends CustomComponent implements Consumer<Event<ComponentEvent<Buerger>>> {

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

        controller.getEventbus().on(T(ComponentEvent.class),this);

        FormLayout layout = new FormLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        layout.setMargin(true);

        // headline
        Label headline = new Label(controller.resolveRelative(getFormPath(SimpleAction.create, Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

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
        
        TextField firstField = controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.VORNAME, Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.VORNAME, Type.input_prompt)),
                Buerger.VORNAME, BuergerViewController.I18N_BASE_PATH);
        firstField.focus();
        firstField.addValidator(val0);
        firstField.addValidator(new StringLengthValidator(controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.validation)),1,Integer.MAX_VALUE, false));
        layout.addComponent(firstField);
        
        // alle anderen Felder
        TextField secField = controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.input_prompt)),
                Buerger.NACHNAME, BuergerViewController.I18N_BASE_PATH);
        secField.addValidator(val0);
        secField.addValidator(new StringLengthValidator(controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.validation)),1,Integer.MAX_VALUE, false));
        layout.addComponent(secField);
        DateField birthdayfield = controller.getUtil().createFormDateField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.GEBURTSDATUM, Type.label)),
                Buerger.GEBURTSDATUM, BuergerViewController.I18N_BASE_PATH);
        String errorMsg = controller.resolveRelative(getEntityFieldPath(Buerger.GEBURTSDATUM, Type.validation));
        birthdayfield.addValidator(ValidatorFactory.getValidator("DateRange",errorMsg, "start",null));
        birthdayfield.addValidator(ValidatorFactory.getValidator("Null",controller.resolveRelative(getEntityFieldPath(Buerger.NACHNAME, Type.validation)),"false"));
        layout.addComponent(birthdayfield); 
        
        
        layout.addComponent(buttonLayout);
        // die Schaltfläche zum Aktualisieren
        String update = controller.resolveRelative(getFormPath(SimpleAction.save, Component.button, Type.label));
        Button updateButton = new Button(update, (Button.ClickEvent click) -> {
            try {
                binder.commit();
                Buerger entity = binder.getItemDataSource().getBean();
                controller.postEvent(new AppEvent<Buerger>(entity, EventType.UPDATE).navigateTo(navigateTo).from(this.from));
            } catch (FieldGroup.CommitException e) {
                GenericErrorNotification error = new GenericErrorNotification("Fehler","Beim erstellen der Person ist ein Fehler aufgetreten. Bitte füllen Sie alle Felder mit gültigen Werten aus.");
                error.show(Page.getCurrent());
            }
        });
        updateButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        updateButton.setIcon(FontAwesome.PENCIL);
        updateButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        updateButton.setId(String.format("%s_UPDATE_BUTTON_FORM", BuergerViewController.I18N_BASE_PATH));
        buttonLayout.addComponent(updateButton);
        // die Schaltfläche zum Abbrechen
        buttonLayout.addComponent(new GenericCancelButton(
                controller.resolveRelative(getFormPath(SimpleAction.cancel, Component.button, Type.label)),
                new AppEvent<Buerger>(null, EventType.CANCEL).navigateTo(this.back), // hier kann eine 'null' gesetzt werden, weil nichts mehr mit dem Objekt passiert
                this.controller.getEventbus()));
        setCompositionRoot(layout);
    }


    public String getNavigateBack() {
        return back;
    }

    public void setNavigateBack(String navigateBack) {
        this.back = navigateBack;
    }

    @Override
    public void accept(reactor.bus.Event<ComponentEvent<Buerger>> eventWrapper) {
        ComponentEvent event = eventWrapper.getData();

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
}

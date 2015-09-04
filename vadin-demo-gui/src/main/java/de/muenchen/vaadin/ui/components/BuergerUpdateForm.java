package de.muenchen.vaadin.ui.components;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.Navigator;
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
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerComponentEvent;
import de.muenchen.vaadin.ui.components.buttons.ActionButton;
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

/**
 *
 * @author claus
 */
public class BuergerUpdateForm extends CustomComponent implements Consumer<Event<ComponentEvent<Buerger>>> {

    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerUpdateForm.class);

    private final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
    private final BuergerViewController controller;

    private final String navigateTo;
    private final String navigateBack;

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
    public BuergerUpdateForm(BuergerViewController controller, final String navigateTo, String navigateBack) {

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

        controller.registerToComponentEvent(this);

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

        final ActionButton updateButton = new ActionButton(controller, SimpleAction.save, "lasdfölkjef");
        updateButton.addClickListener(clickEvent1 -> {
            try {
                binder.commit();
                Buerger buerger = binder.getItemDataSource().getBean();
                controller.postEvent(new AppEvent<Buerger>(buerger, EventType.UPDATE));
                getNavigator().navigateTo(getNavigateTo());
            } catch (FieldGroup.CommitException e) {
                GenericErrorNotification errorNotification = new GenericErrorNotification("Fehler", "Beim erstellen der Person ist ein Fehler aufgetreten. Bitte füllen Sie alle Felder mit gültigen Werten aus.");
                errorNotification.show(Page.getCurrent());
            }
        });
        buttonLayout.addComponent(updateButton);

        // die Schaltfläche zum Abbrechen
        final ActionButton back = new ActionButton(controller, SimpleAction.back, "egal");
        back.addClickListener(clickEvent -> getNavigator().navigateTo(getNavigateBack()));
        buttonLayout.addComponent(back);

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

    public String getNavigateBack() {
        return navigateBack;
    }

    public Navigator getNavigator() {
        return getController().getNavigator();
    }

    public BuergerViewController getController() {
        return controller;
    }

    public String getNavigateTo() {
        return navigateTo;
    }
}

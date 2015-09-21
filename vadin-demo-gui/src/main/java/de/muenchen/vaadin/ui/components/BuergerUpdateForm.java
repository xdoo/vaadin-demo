package de.muenchen.vaadin.ui.components;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.events.ComponentEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;
import de.muenchen.vaadin.guilib.util.ValidatorFactory;
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

        controller.registerToAllComponentEvents(this);

        FormLayout layout = new FormLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        layout.setMargin(true);

        // headline
        Label headline = new Label(controller.resolveRelative(getFormPath(SimpleAction.create, Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        Validator val0 = ValidatorFactory.getValidator(ValidatorFactory.Type.DIAKRITISCH, controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validationstring)), "true");
        
        TextField firstField = controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.input_prompt)),
                Buerger.Field.vorname.name(), BuergerViewController.I18N_BASE_PATH);
        firstField.focus();
        firstField.addValidator(val0);
        firstField.addValidator(new StringLengthValidator(controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), 1, Integer.MAX_VALUE, false));
        layout.addComponent(firstField);
        
        // alle anderen Felder
        TextField secField = controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.input_prompt)),
                Buerger.Field.nachname.name(), BuergerViewController.I18N_BASE_PATH);
        secField.addValidator(val0);
        secField.addValidator(new StringLengthValidator(controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), 1, Integer.MAX_VALUE, false));
        layout.addComponent(secField);
        ListSelect ls = controller.getUtil().createFormComboBox(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.augenfarbe.name(), Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.augenfarbe.name(), Type.input_prompt)),
                Buerger.Field.augenfarbe.name(), BuergerViewController.I18N_BASE_PATH);
        layout.addComponent(ls);
        DateField birthdayfield = controller.getUtil().createFormDateField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), Type.label)),
                Buerger.Field.geburtsdatum.name(), BuergerViewController.I18N_BASE_PATH);
        String errorMsg = controller.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), Type.validation));
        birthdayfield.addValidator(ValidatorFactory.getValidator(ValidatorFactory.Type.DATE_RANGE, errorMsg, "start", null));
        birthdayfield.addValidator(ValidatorFactory.getValidator(ValidatorFactory.Type.NULL, controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), "false"));
        layout.addComponent(birthdayfield); 
        
        
        layout.addComponent(buttonLayout);
        // die Schaltfläche zum Aktualisieren

        final ActionButton updateButton = new ActionButton(controller, SimpleAction.save, "lasdfölkjef");
        updateButton.addClickListener(clickEvent1 -> {
            try {
                binder.commit();
                Buerger buerger = binder.getItemDataSource().getBean();
                controller.postEvent(controller.buildAppEvent(EventType.UPDATE).setEntity(buerger));
                getNavigator().navigateTo(getNavigateTo());
            } catch (FieldGroup.CommitException e) {
                GenericErrorNotification error = new GenericErrorNotification(controller.resolveRelative(getNotificationPath(I18nPaths.NotificationType.failure, SimpleAction.save, Type.label)),
                        controller.resolveRelative(getNotificationPath(I18nPaths.NotificationType.failure, SimpleAction.save, Type.text)));
                error.show(Page.getCurrent());
            }
        });
        buttonLayout.addComponent(updateButton);

        // die Schaltfläche zum Abbrechen
        final ActionButton back = new ActionButton(controller, SimpleAction.back, "egal");
        back.addClickListener(clickEvent -> getNavigator().navigateTo(getNavigateBack()));
        buttonLayout.addComponent(back);

        setCompositionRoot(layout);
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

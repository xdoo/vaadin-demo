package de.muenchen.vaadin.ui.components;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.api.domain.Augenfarbe;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericErrorNotification;
import de.muenchen.vaadin.guilib.util.ValidatorFactory;
import de.muenchen.vaadin.services.BuergerI18nResolver;
import de.muenchen.vaadin.services.model.BuergerDatastore;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.bus.Event;
import reactor.fn.Consumer;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getEntityFieldPath;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;

/**
 * @author claus
 */
public class BuergerUpdateForm extends CustomComponent implements Consumer<Event<BuergerDatastore>> {

    /**
     * Logger
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerUpdateForm.class);

    private final BeanFieldGroup<Buerger> binder = new BeanFieldGroup<Buerger>(Buerger.class);
    private final BuergerViewController controller;
    private final BuergerI18nResolver resolver;

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
    public BuergerUpdateForm(BuergerViewController controller, BuergerI18nResolver resolver, final String navigateTo, String navigateBack) {

        this.controller = controller;
        this.resolver = resolver;
        this.navigateTo = navigateTo;
        this.navigateBack = navigateBack;

        // create form
        this.createForm();

    }

    /**
     * Erzeugt das eigentliche Formular.
     */
    private void createForm() {

        controller.getEventbus().on(controller.getResponseKey().toSelector(), this);

        FormLayout layout = new FormLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        layout.setMargin(true);

        // headline
        Label headline = new Label(resolver.resolveRelative(getFormPath(SimpleAction.create, I18nPaths.Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        Validator val0 = ValidatorFactory.getValidator(ValidatorFactory.Type.DIAKRITISCH, resolver.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validationstring)), "true");

        TextField firstField = controller.getUtil().createFormTextField(binder,
                resolver.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.label)),
                resolver.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.input_prompt)),
                Buerger.Field.vorname.name(),
                BuergerI18nResolver.I18N_BASE_PATH);
        firstField.focus();
        firstField.addValidator(val0);
        firstField.addValidator(new StringLengthValidator(resolver.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), 1, Integer.MAX_VALUE, false));
        layout.addComponent(firstField);

        // alle anderen Felder
        TextField secField = controller.getUtil().createFormTextField(binder,
                resolver.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.label)),
                resolver.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.input_prompt)),
                Buerger.Field.nachname.name(),
                BuergerI18nResolver.I18N_BASE_PATH);
        secField.addValidator(val0);
        secField.addValidator(new StringLengthValidator(resolver.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), 1, Integer.MAX_VALUE, false));
        layout.addComponent(secField);
        ComboBox cb = controller.getUtil().createFormComboBox(binder,
                resolver.resolveRelative(getEntityFieldPath(Buerger.Field.augenfarbe.name(), Type.label)),
                Buerger.Field.augenfarbe.name(),
                Augenfarbe.class);
        layout.addComponent(cb);
        DateField birthdayfield = controller.getUtil().createFormDateField(binder,
                resolver.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), Type.label)),
                Buerger.Field.geburtsdatum.name(), BuergerI18nResolver.I18N_BASE_PATH);
        String errorMsg = resolver.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), Type.validation));
        birthdayfield.addValidator(ValidatorFactory.getValidator(ValidatorFactory.Type.DATE_RANGE, errorMsg, "start", null));
        birthdayfield.addValidator(ValidatorFactory.getValidator(ValidatorFactory.Type.NULL, resolver.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), "false"));
        layout.addComponent(birthdayfield);


        layout.addComponent(buttonLayout);
        // die Schaltfläche zum Aktualisieren

        final ActionButton updateButton = new ActionButton(resolver, SimpleAction.save, "lasdfölkjef");
        updateButton.addClickListener(clickEvent1 -> {
            try {
                binder.commit();
                Buerger buerger = binder.getItemDataSource().getBean();
                controller.getEventbus().notify(controller.getRequestKey(RequestEvent.UPDATE), reactor.bus.Event.wrap(buerger));
                getNavigator().navigateTo(getNavigateTo());
            } catch (FieldGroup.CommitException e) {
                GenericErrorNotification error = new GenericErrorNotification(resolver.resolveRelative(getNotificationPath(I18nPaths.NotificationType.failure, SimpleAction.save, Type.label)),
                        resolver.resolveRelative(getNotificationPath(I18nPaths.NotificationType.failure, SimpleAction.save, Type.text)));
                error.show(Page.getCurrent());
            }
        });
        buttonLayout.addComponent(updateButton);

        // die Schaltfläche zum Abbrechen
        final ActionButton back = new ActionButton(resolver, SimpleAction.back, "egal");
        back.addClickListener(clickEvent -> getNavigator().navigateTo(getNavigateBack()));
        buttonLayout.addComponent(back);

        setCompositionRoot(layout);
    }

    @Override
    public void accept(reactor.bus.Event<BuergerDatastore> eventWrapper) {
        BuergerDatastore model = eventWrapper.getData();
        model.getSelectedBuerger().ifPresent(binder::setItemDataSource);
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

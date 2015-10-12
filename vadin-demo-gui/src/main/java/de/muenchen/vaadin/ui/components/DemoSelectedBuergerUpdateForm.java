package de.muenchen.vaadin.ui.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.ActionButton;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.read.SelectedBuergerForm;
import de.muenchen.vaadin.ui.controller.BuergerViewController;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;

/**
 *
 * @author claus
 */
public class DemoSelectedBuergerUpdateForm extends SelectedBuergerForm {

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
    public DemoSelectedBuergerUpdateForm(BuergerViewController controller, final String navigateTo, String navigateBack) {
        super(controller, controller.getEventbus());

        this.controller = controller;
        this.navigateTo = navigateTo;
        this.navigateBack = navigateBack;

        init();
    }

    private void init() {
        final Label headline = createHeadline();
        getFormLayout().addComponent(headline, 0);

        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        final ActionButton backButton = createBackButton();
        buttons.addComponent(backButton);

        final ActionButton updateButton = createUpdateButton();
        buttons.addComponent(updateButton);

        getFormLayout().addComponent(buttons);
    }

    private ActionButton createUpdateButton() {
        final ActionButton updateButton = new ActionButton(getI18nResolver(), SimpleAction.update);

        final BuergerSingleActions buergerSingleActions = new BuergerSingleActions(this::getBuerger, getEventBus());
        updateButton.addClickListener(buergerSingleActions::update);

        final NavigateActions navigateActions = new NavigateActions(getController().getNavigator(), getController().getEventbus(), getNavigateTo());
        updateButton.addClickListener(navigateActions::navigate);

        return updateButton;
    }

    private ActionButton createBackButton() {
        final ActionButton backButton = new ActionButton(getI18nResolver(), SimpleAction.back);

        final NavigateActions navigateActions = new NavigateActions(getController().getNavigator(), getController().getEventbus(), getNavigateBack());
        backButton.addClickListener(navigateActions::navigate);

        return backButton;
    }

    private Label createHeadline() {
        final Label headline = new Label(getI18nResolver().resolveRelative(getFormPath(SimpleAction.update, I18nPaths.Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        return headline;
    }

    /*
    private void createForm() {

        controller.getEventbus().on(controller.getResponseKey().toSelector(), this);

        FormLayout layout = new FormLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        layout.setMargin(true);

        // headline
        Label headline = new Label(controller.resolveRelative(getFormPath(SimpleAction.create, I18nPaths.Component.headline, Type.label)));
        headline.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(headline);

        Validator val0 = ValidatorFactory.getValidator(ValidatorFactory.Type.DIAKRITISCH, controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validationstring)), "true");
        
        TextField firstField = controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.vorname.name(), Type.input_prompt)),
                Buerger.Field.vorname.name(),
                BuergerViewController.I18N_BASE_PATH);
        firstField.focus();
        firstField.addValidator(val0);
        firstField.addValidator(new StringLengthValidator(controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), 1, Integer.MAX_VALUE, false));
        layout.addComponent(firstField);
        
        // alle anderen Felder
        TextField secField = controller.getUtil().createFormTextField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.label)),
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.input_prompt)),
                Buerger.Field.nachname.name(),
                BuergerViewController.I18N_BASE_PATH);
        secField.addValidator(val0);
        secField.addValidator(new StringLengthValidator(controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), 1, Integer.MAX_VALUE, false));
        layout.addComponent(secField);
        ComboBox cb = controller.getUtil().createFormComboBox(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.augenfarbe.name(), Type.label)),
                Buerger.Field.augenfarbe.name(),
                Augenfarbe.class);
        layout.addComponent(cb);
        DateField birthdayfield = controller.getUtil().createFormDateField(binder,
                controller.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), Type.label)),
                Buerger.Field.geburtsdatum.name(), BuergerViewController.I18N_BASE_PATH);
        String errorMsg = controller.resolveRelative(getEntityFieldPath(Buerger.Field.geburtsdatum.name(), Type.validation));
        birthdayfield.addValidator(ValidatorFactory.getValidator(ValidatorFactory.Type.DATE_RANGE, errorMsg, "start", null));
        birthdayfield.addValidator(ValidatorFactory.getValidator(ValidatorFactory.Type.NULL, controller.resolveRelative(getEntityFieldPath(Buerger.Field.nachname.name(), Type.validation)), "false"));
        layout.addComponent(birthdayfield); 
        
        
        layout.addComponent(buttonLayout);
        // die Schaltfläche zum Aktualisieren

        final ActionButton updateButton = new ActionButton(controller, SimpleAction.save);
        updateButton.addClickListener(clickEvent1 -> {
            try {
                binder.commit();
                Buerger buerger = binder.getItemDataSource().getBean();
                controller.getEventbus().notify(controller.getRequestKey(RequestEvent.UPDATE), reactor.bus.Event.wrap(buerger));
                getNavigator().navigateTo(getNavigateTo());
            } catch (FieldGroup.CommitException e) {
                GenericErrorNotification error = new GenericErrorNotification(controller.resolveRelative(getNotificationPath(I18nPaths.NotificationType.failure, SimpleAction.save, Type.label)),
                        controller.resolveRelative(getNotificationPath(I18nPaths.NotificationType.failure, SimpleAction.save, Type.text)));
                error.show(Page.getCurrent());
            }
        });
        buttonLayout.addComponent(updateButton);

        // die Schaltfläche zum Abbrechen
        final ActionButton back = new ActionButton(controller, SimpleAction.back);
        back.addClickListener(clickEvent -> getNavigator().navigateTo(getNavigateBack()));
        buttonLayout.addComponent(back);

        setCompositionRoot(layout);
    }
*/

    public String getNavigateBack() {
        return navigateBack;
    }

    public BuergerViewController getController() {
        return controller;
    }

    public String getNavigateTo() {
        return navigateTo;
    }
}
